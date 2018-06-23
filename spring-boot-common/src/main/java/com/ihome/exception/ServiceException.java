package com.ihome.exception;

import com.alibaba.fastjson.JSON;

import com.ihome.constant.ErrorCode;
import com.ihome.param.BaseResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceException extends RuntimeException {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceException.class);
	private static final long serialVersionUID = -5325392504946334907L;

	/**
	 * ServiceException
	 */
	public ServiceException() {
		super();
	}

	public ServiceException(ErrorCode errorCode) {
		this(errorCode.getCode(), errorCode.getDesc());
	}

	public ServiceException(ErrorCode errorCode, String msg) {
		this(errorCode.getCode(), errorCode.getDesc() + "[" + msg + "]");
	}

	/**
	 * @param code
	 * @param msg
	 */
	public ServiceException(String code, String msg) {
		super(initErrorMessage(code, msg, null));
	}

	/**
	 * @param code
	 * @param msg
	 * @param body
	 */
	public ServiceException(String code, String msg, Object body) {
		super(initErrorMessage(code, msg, body));
	}

	private static String initErrorMessage(String code, String msg, Object body) {
		String message = "未知异常.";
		if (!StringUtils.isEmpty(msg)) {
			message = msg;
		}
		BaseResponse response = new BaseResponse(code, message, body);
		String responseJson = null;
		try {
			responseJson = JSON.toJSONString(response);
			LOGGER.error(responseJson);
		} catch (Exception e) {
			LOGGER.error("发生JSON转换错误", e);
		}
		return responseJson;
	}
}