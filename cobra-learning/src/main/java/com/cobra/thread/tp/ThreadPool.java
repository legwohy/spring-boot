package com.cobra.thread.tp;
/**
 * 面向接口编程
 */
import java.util.List;

/**
 * FIFO :first input first output
 * LIFO :last input first output
 * 线程池 wait-notifyAll 线程调度
 * execute 放将待执行的任务放在LinkedBlockingQueue(FIFO)队列中 并唤醒所有等待的工作线程
 * WorkThread 内部工作线程 取出LinkedBlockingQueue队列中的任务 并执行 取出任务 有阻塞线程 wait
 */
public interface ThreadPool {
	// 执行一个Runnable类型的任务
	void execute(Runnable task);

	void execute(Runnable[] tasks);

	void execute(List<Runnable> tasks);

	// 返回以执行任务的个数
	int getExecuteTaskNumber();

	// 返回任务队列的长度，即还没处理的任务个数
	int getWaitTaskNumber();

	// 返回工作线程的个数
	int getWorkThreadNumber();

	// 关闭线程池
	void destroy();
}
