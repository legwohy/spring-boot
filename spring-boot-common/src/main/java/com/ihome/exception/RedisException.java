package com.xjx.exception;

public class RedisException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 605795238950026910L;

	private String code;
	private String msg;

	public final static String DEFAULT_CODE = "000";
	public final static String DEFAULT_MSG = "读取redis报错";

	public RedisException() {
		this.code = DEFAULT_CODE;
		this.msg = DEFAULT_MSG;
	}

	public RedisException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}