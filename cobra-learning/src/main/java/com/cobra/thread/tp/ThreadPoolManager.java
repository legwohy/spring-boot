package com.cobra.thread.tp;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 单例
 * 1、线程数
 * 2、待执行线程队列
 * 3、待执行的任务
 */
public class ThreadPoolManager implements ThreadPool {

	// 线程池中默认线程的个数为5
	private static int workerNum = 5;
	// 工作线程数组
	WorkThread[] workThrads;
	// 在执行的任务数量 volatile
	private static volatile int executeTaskNumber = 0;

	// 任务队列，作为一个缓冲,List线程不安全 BlockingQueue 线程安全的队列
	/**
	 * 阻塞队列 线程安全的队列
	 * 		插入数据 只有队列未满才能插入 满时只能等待  put
	 *		取出数据 若队列为空 将会被阻塞 take
	 */
	private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
	private static ThreadPoolManager threadPool;
	private AtomicLong threadNum = new AtomicLong();

	/**
	 * 工作线程数组 默认五个线程 超过五个线程 扩容
	 */
	private ThreadPoolManager() {
		this(workerNum);
	}
	private ThreadPoolManager(int workerNum2) {
		if (workerNum2 > 0) {
			workerNum = workerNum2;
		}
		// 工作线程池初始化 开辟了一个连续的空间
		workThrads = new WorkThread[workerNum];

		for (int i = 0; i < workerNum; i++) {
			// 每个空间的对象初始化
			workThrads[i] = new WorkThread();
			workThrads[i].setName("ThreadPool-worker" + threadNum.incrementAndGet());
			System.out.println("初始化线数: " + (i + 1)+"/"+(workerNum) + "  ----当前线程名称是：" + workThrads[i].getName());
			workThrads[i].start();
		}
	}

	/**
	 * 同步 将单个任务放在队列中 并唤醒线程
	 * param task 待执行的任务
	 */
	@Override
	public void execute(Runnable task) {
		synchronized (taskQueue) {
			try {
				taskQueue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 taskQueue.notifyAll();
			// taskQueue.notify();
		}

	}

	@Override
	public void execute(Runnable[] tasks) {
		synchronized (taskQueue) {
			for (Runnable task : tasks)
				try {
					taskQueue.put(task);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			taskQueue.notifyAll();
			// taskQueue.notify();
		}

	}

	@Override
	public void execute(List<Runnable> tasks) {
		synchronized (taskQueue) {
			for (Runnable task : tasks)
				try {
					taskQueue.put(task);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			taskQueue.notifyAll();
			// taskQueue.notify();
		}
	}

	/**
	 * 单例模式提供的公共方法 获取线程池
	 * jdk 提供两种方式获取线程池
	 * @return
	 */
	public static ThreadPool getThreadPool() {
		return getThreadPool(ThreadPoolManager.workerNum);
	}
	public static ThreadPool getThreadPool(int workerNum) {
		if (workerNum <= 0) {
			workerNum = ThreadPoolManager.workerNum;
		}
		if (threadPool == null) {
			threadPool = new ThreadPoolManager(workerNum);
		}
		return threadPool;
	}

	@Override
	public int getExecuteTaskNumber() {
		return executeTaskNumber;
	}

	@Override
	public int getWaitTaskNumber() {
		return taskQueue.size();
	}

	@Override
	public int getWorkThreadNumber() {
		return workerNum;
	}

	/**
	 * 销毁线程池
	 */
	@Override
	public void destroy() {
		while (!taskQueue.isEmpty()) {
			try {
				// 不断的检测任务队列是否全部执行
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		for (int i = 0; i < workerNum; i++) {
			workThrads[i].stopWorker();
			workThrads[i] = null;
		}
		threadPool = null;
		taskQueue.clear();
	}

	@Override
	public String toString() {
		return "当前工作线程数量为:" + workerNum + "  已经完成任务数:" + executeTaskNumber + "  等待任务数:" + getWaitTaskNumber();
	}

	/**
	 * wait/notify/notifyAll 获得锁后，阻塞/唤醒/唤醒所有当前线程
	 * 配合synchronized 使用 在synchronized 代码快中执行
	 * 当线程执行到wait代码块时会释放当前锁并进入等待状态，
	 * 只有当执行到notify/notifyAll 代码块时 才能唤醒一个或多个等待的线程，
	 * 然后继续往下执行, 直到执行完synchronized代码块或wait关键字再次释放锁
	 * 推论 正常情况下
	 * 			1、代码遇到wait 任务结束 遇到notifyAll 代码会继续执行
	 *
	 * 内部类，工作线程 核心
	 * 锁 taskQueue 任务队列 在队列的取和放方法中wait和notifyAll
	 * WorkThread.run 工作线程里面取出任务 任务为空 阻塞 wait
	 * executor 存放任务 并唤醒等待线程 notify
	 */
	private class WorkThread extends Thread {
		// 该工作线程是否有效，用于结束该工作线程
		private boolean isRunning = true;

		/*
		 * 关键所在啊，如果任务队列不空，则取出任务执行，若任务队列空，则等待
		 */
		@Override
		public void run() {
			// 接收队列当中的任务对象 任务对象Runnable类型
			Runnable r = null;
			while (isRunning) {// false表示线程被关闭 手动调用关闭线程
				// 队列同步机制synchronized
				synchronized (taskQueue) {
					while (isRunning && taskQueue.isEmpty()) {// 队列为空 当前线程将会被阻塞
						try {
							taskQueue.wait(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// 线程不为空
					if (!taskQueue.isEmpty())
						try {
							// 取出任务
							r = taskQueue.take();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

				}

				// 执行线程
				if (r != null) {
					((Runnable) r).run();// 执行任务
				}
				executeTaskNumber++;
				r = null;
			}
		}

		// 停止工作，让该线程自然执行完run方法，自然结束
		public void stopWorker() {
			isRunning = false;
		}

	}


}
