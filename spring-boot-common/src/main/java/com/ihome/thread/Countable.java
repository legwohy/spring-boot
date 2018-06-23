package com.xjx.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于方便类计数，比方向计算该类创建了多少对象，或创建了多少线程。
 * @author xuehan
 */
public interface Countable {
	/**
	 * 原子计数
	 */
	 public static  AtomicInteger nextId = new AtomicInteger();
	
}
