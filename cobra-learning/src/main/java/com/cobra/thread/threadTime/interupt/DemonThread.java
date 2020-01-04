package com.cobra.thread.threadTime.interupt;

/**
 *
 * 线程超时设置
 * <a link='https://blog.csdn.net/shimiso/article/details/8964414'/>
 * <a link='https://www.jianshu.com/p/de77103b9ea7'/>
 * <p>
     当 对一个线程调用了 interrupt()之后，如果该线程处于被阻塞状态（比如执行了wait、sleep或join等方法），
      那么会立即退出阻塞状态，并抛出一个InterruptedException异常，在代码中catch这个异常进行后续处理。
      如果线程一直处于运行状态，那么只会把该线程的中断标志设置为 true，仅此而已，所以interrupt()并不能真正的中断线程，
      不过在rpc调用的场景中，请求线程一般都处于阻塞状态，等待数据返回，这时interrupt()方法是可以派上用场的。
  </p>
 * 使用 interrupt() 前提是线程处于阻塞状态 否则 会不起作用
 */
public class DemonThread {
    public static void main(String[] args) {

        System.out.println("... main start...");

        // 任务执行时间
        Task task = new Task("任务线程", 3);

        Thread taskThread = new Thread(task);


        // 守护线程的时间应该小于 任务线程的时间 才能终端超时
        Thread daemonThread = new Thread(new Daemon(taskThread, 1));
        daemonThread.setDaemon(true);// 默认为false 非守护线程 必须在start 方法之前设置

        taskThread.start();
        daemonThread.start();


        System.out.println("... main end...");


    }

}

/**
 * 阻塞: sleep、 wait、yield、join
 * 任务线程
 * 在rpc调用的场景中，请求线程一般都处于阻塞状态，等待数据返回，这时interrupt()方法是可以派上用场的
 */
class Task implements Runnable {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务执行最大时间
     */
    private Integer taskTimeout;

    public Task(String taskName, Integer taskTimeout) {
        this.taskName = taskName;
        this.taskTimeout = taskTimeout;
    }


    @Override
    public void run() {
        try {

            System.out.println("执行任务...start...");

            // 如果线程被打断 在sleep 或wait等阻塞时会抛出异常 InterruptedException(打断异常)
            Thread.sleep(taskTimeout * 1000);

            System.out.println("执行任务...end...");
        } catch (Exception e) {
            System.out.println(taskName + ":任务执行超时 异常");

            // 注意这里如果不return的话，线程还会继续执行，所以任务超时后在这里处理结果然后返回
            return;
        }
    }
}

/**
 * 守护线程
 */
class Daemon implements Runnable {

    /**
     * 工作线程
     */
    private Thread taskThread;

    /**
     * 超时时间 单位 s
     */
    private Integer timeout;

    public Daemon(Thread taskThread, Integer timeout) {
        this.taskThread = taskThread;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 守护线程设置超时时间
                Thread.sleep(timeout * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("守护线程被打断");
            }
            // 打断任务线程 一旦任务线程 被阻塞 将会抛异常
            taskThread.interrupt();
        }
    }
}
