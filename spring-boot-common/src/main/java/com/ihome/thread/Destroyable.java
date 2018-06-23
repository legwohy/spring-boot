package com.ihome.thread;

import javax.security.auth.DestroyFailedException;

public interface Destroyable {
	/**
	 * 销毁这个对象
	 * <p>这个对象关联的信息和资源将会被清楚或者关闭，以后调用这个对象的其它资源方法将会抛出<code>IllegalStateException</code></p>
	 * <p>
	 *  这个方法仅仅是告诉对象开始清楚或销毁对象，是否清楚或销毁结束应该根据isDestroyed()去做决定</p>
	 * @throws DestroyFailedException
	 *
	 */
    void startDestroy() throws DestroyFailedException;
    /**
     * 判断这个对象是否已经被销毁
     * 
     * @return true 代表这个<code>对象<code>已经被销毁，false是其它情况
     */
    boolean isDestroyed();
}
