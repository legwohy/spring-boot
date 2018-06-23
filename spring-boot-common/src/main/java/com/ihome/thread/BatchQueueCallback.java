package com.xjx.thread;

import java.util.List;

/**
 * 批量数据回调接口
 * @author xuehan
 *
 * @param <T>
 */
public interface BatchQueueCallback<T> {

	/**
	 * 用于接收批量数据
	 * @param list 批量数据
	 */
	public abstract  void batch(List<T> list);
}
