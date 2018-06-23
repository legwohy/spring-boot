package com.xjx.thread;

import javax.security.auth.DestroyFailedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 批量队列</br> 把放入此队列的元素,按照指定设置的数量成批回调同步或异步给接收者
 * 
 * <pre>
 * //simble demo
 * public class BatchQueueTest implements BatchQueueCallback&lt;String&gt; {
 * 	public static void main(String[] args) throws InterruptedException {
 * 		BatchQueueTest t = new BatchQueueTest();
 * 		t.test();
 * 	}
 * 
 * 	private void test() throws InterruptedException {
 * 		BatchQueue&lt;String&gt; q = new BatchQueue&lt;String&gt;(this);
 * 		while (true) {
 * 			long s = System.currentTimeMillis();
 * 			for (int i = 0; i &lt; 6000000; i++) {
 * 				q.add(&quot;i:&quot; + i);
 * 			}
 * 			long e = System.currentTimeMillis();
 * 			System.out.println(e - s);
 * 			try {
 * 				Thread.sleep(1000);
 * 			} catch (InterruptedException ex) {
 * 				ex.printStackTrace();
 * 			}
 * 		}
 * 	}
 * 
 * 	public void batch(List&lt;String&gt; list) {
 * 		System.out.println(list);
 * 	}
 * }
 *</pre>
 * 
 * @since v1.0.0
 * @author xuehan <br/>
 * @see BatchQueueCallback
 * @param <T>
 */
public class BatchQueue<T> implements Countable, Destroyable {

	private ArrayBlockingQueue<T> cacheQueue = new ArrayBlockingQueue<T>(50000);

	private static final int DEFALUT_EXCEED_TIME = 2000;
	private static final int BATCH_DATA_MAC_COUNT = 100;
	private static final String DEFALUT_THREAD_NAME = "batch queue loop :" + nextId.addAndGet(1);
	private int exceedTimeByMills;
	private int batchDataMaxCount;
	private BatchQueueCallback<T> callback;
	private List<T> dataList = null;
	private ExecutorService service = null;
	private final boolean serviceIsNotNUll;
	private Thread thread;
	private volatile boolean destroy = false;
	private volatile boolean terminated = false;


	/**
	 * 创建一个批量队列，按默认设置同步回调队列数据 默认超时时间:<b>2000</b>（单位:毫秒）</br> 默认超过数量:<b>100</b>
	 * 
	 * @param callback
	 *            批量队列数据接收者
	 */
	public BatchQueue(BatchQueueCallback<T> callback) {
		this(DEFALUT_EXCEED_TIME, BATCH_DATA_MAC_COUNT, callback, DEFALUT_THREAD_NAME);
	}

	/**
	 * 创建一个批量队列，按默认设置同步回调队列数据 默认超时时间:<b>2000</b>（单位:毫秒）</br> 默认超过数量:<b>100</b>
	 * 
	 * @param callback
	 *            批量队列数据接收者
	 */
	public BatchQueue(BatchQueueCallback<T> callback, String threadName) {
		this(DEFALUT_EXCEED_TIME, BATCH_DATA_MAC_COUNT, callback, threadName);
	}

	/**
	 * 创建一个批量队列，按指定设置同步回调队列数据
	 * 
	 * @param exceedTimeByMills
	 *            超过一定时间，将批量队列回调给接收者（单位:毫秒） </br>0和负数是禁用时间。
	 * @param batchDataMaxCount
	 *            超过一定数量，将批量队列回调给接收者
	 * @param callback
	 *            批量队列数据接收者
	 */
	public BatchQueue(int exceedTimeByMills, int batchDataMaxCount, BatchQueueCallback<T> callback, String threadName) {
		this(exceedTimeByMills, batchDataMaxCount, callback, null, threadName);
	}

	/**
	 * 创建一个批量队列，按指定设置，通过service异步调用给接收者
	 * 
	 * @param exceedTimeByMills
	 *            超过一定时间，将批量队列回调给接收者（单位:毫秒） </br>0和负数是禁用时间。
	 * @param batchDataMaxCount
	 *            超过一定数量，将批量队列回调给接收者
	 * @param callback
	 *            批量队列数据接收者
	 */
	public BatchQueue(int exceedTimeByMills, int batchDataMaxCount, BatchQueueCallback<T> callback,
                      ExecutorService service, String threadName) {
		super();
		if (exceedTimeByMills <= 0) {
			exceedTimeByMills = -1;
		}
		this.exceedTimeByMills = exceedTimeByMills;
		this.batchDataMaxCount = batchDataMaxCount;
		this.callback = callback;
		// increase value for unknown error
		dataList = new ArrayList<T>(batchDataMaxCount + 5);
		if (service != null) {
			this.serviceIsNotNUll = true;
		} else {
			this.serviceIsNotNUll = false;
		}
		this.service = service;
		start(threadName);
	}

	private void start(String threadName) {
		BatchLoopRunnable runnable = new BatchLoopRunnable();
		String tname;
		// 设置线程名字，开始执行
		if (threadName == null) {
			tname = DEFALUT_THREAD_NAME;
		} else {
			tname = threadName;
		}
		thread = new Thread(new RunnableRenaming(runnable, tname));
		thread.start();
	}

	public void add(T t) throws InterruptedException ,IllegalStateException{
		if (destroy) {
			throw new IllegalStateException("batch queue server is destroy");
		}
		cacheQueue.put(t);
	}

	class BatchLoopRunnable implements Runnable {
		public void run() {
			long lastTime = System.currentTimeMillis();
			final int rsdata_time_interval_by_ms = BatchQueue.this.exceedTimeByMills;
			final int maxCount = BatchQueue.this.batchDataMaxCount;
			final ArrayBlockingQueue<T> cacheQueue = BatchQueue.this.cacheQueue;
			T data = null;
			while (true) {
				try {
					List<T> dataListVar = BatchQueue.this.dataList;
					switch (rsdata_time_interval_by_ms) {
					// 禁用时间
					case -1:
						data = cacheQueue.take();
						dataListVar.add(data);
						if (dataListVar.size() > maxCount) {
							batchCallback(dataListVar);
						}
						break;
					default:
						data = cacheQueue.poll(rsdata_time_interval_by_ms, TimeUnit.MILLISECONDS);
						if (data == null) {
							// 超时
							lastTime = batchCallback(dataListVar);
							continue;
						} else {
							dataListVar.add(data);
							long currTime = System.currentTimeMillis();
							if (currTime - lastTime >= rsdata_time_interval_by_ms) {
								lastTime = batchCallback(dataListVar);
								continue;
							}
						}
						if (dataListVar.size() > maxCount) {
							lastTime = batchCallback(dataListVar);
							continue;
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				if (destroy && cacheQueue.size() == 0) {
					terminated=true;
					break;
				}
			}
		}

		private synchronized long batchCallback(final List<T> dataList) {
			if (dataList != null && dataList.size() > 0) {
				try {
					if (serviceIsNotNUll) {
						service.execute(new Runnable() {
							public void run() {
								callback.batch(dataList);
							}
						});
					} else {
						callback.batch(dataList);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					BatchQueue.this.dataList = new ArrayList<T>(batchDataMaxCount);
				}
			}
			return System.currentTimeMillis();
		}
	}

	/**
	 * 
	 * @return 超时时间
	 */
	public long getExceedTimeByMills() {
		return exceedTimeByMills;
	}

	/**
	 * 
	 * @return 超过数量
	 */
	public int getBatchDataMaxCount() {
		return batchDataMaxCount;
	}

	@Override
	public void startDestroy() throws DestroyFailedException {
		if(destroy){
			return;
		}
		synchronized (this) {
			try {
				if(destroy){
					return;
				}
				destroy = true;
				service.shutdown();
			} catch (Exception e) {
				throw new DestroyFailedException(e.getMessage());
			}
		}
	}

	@Override
	public synchronized boolean isDestroyed() {
		if(terminated&&service.isTerminated()){
			return true;
		}
		return false;
	}
}
