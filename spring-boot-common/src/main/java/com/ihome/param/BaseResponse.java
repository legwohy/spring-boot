
package com.xjx.param;

import java.io.Serializable;

import static com.xjx.constant.ErrorCode.SUCESS;

public class BaseResponse implements Serializable {

	private static final long serialVersionUID = -620727297442556977L;
	// 返回状态吗
	private String code = SUCESS.getCode();
	// 返回说明
	private String msg = SUCESS.getDesc();
	// 自定义返回数据
	private Object data;

	public BaseResponse() {
	}

	public BaseResponse(Object data) {
		this.data = data;
	}

	public BaseResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public BaseResponse(String code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BaseResponse{" + "code='" + code + '\'' + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}
}
