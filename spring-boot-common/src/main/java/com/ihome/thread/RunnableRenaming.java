package com.ihome.thread;

/**
 * 线程命名类，用于对执行runnable的线程命名
 * 
 * @author xuehan
 * 
 */
public class RunnableRenaming implements Runnable {
	private final Runnable runnable;
	private final String proposedThreadName;
	private final boolean abandonOldName;
	/**
	 * 
	 * @param runnable
	 *            目标runnable
	 * @param proposedThreadName
	 *            线程名字
	 */
	public RunnableRenaming(Runnable runnable, String proposedThreadName) {
		this(runnable, proposedThreadName, false);
	}

	/**
	 * 
	 * @param runnable
	 *            目标runnable
	 * @param proposedThreadName
	 *            线程名字
	 * @param proposedThreadName
	 *            是否丢弃线程以前的名字， <code>true</code>代表线程将一直使用
	 *            <code>proposedThreadName</code>, <code>false</code>代表线程在任务
	 *            <code>runnable</code>结束后还原以前的线程名字
	 */
	public RunnableRenaming(Runnable runnable, String proposedThreadName, boolean abandonOldName) {
		super();
		this.runnable = runnable;
		this.proposedThreadName = proposedThreadName;
		this.abandonOldName=abandonOldName;
	}

	public void run() {
		final Thread currentThread = Thread.currentThread();
		final String oldName = currentThread.getName();
		final String newName = proposedThreadName;
		boolean renamed = false;
		try {
			currentThread.setName(newName);
			renamed = true;
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		try {
			runnable.run();
		} finally {
			//如果命名成功并且不丢弃原先线程名字
			if (renamed&&!abandonOldName) {
				currentThread.setName(oldName);
			}
		}
	}
}
