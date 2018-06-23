
package com.xjx.constant;

/**
 * 
 * 功能描述: 错误返回码
 * 
 * @逻辑说明: 增加描述代码逻辑
 * 
 * @牵涉到的配置项: 若果代码中逻辑牵涉到配置项在这里列出
 *
 * @编码实现人员 李鸿
 * @需求提出人员 填写需求填写人员
 * @实现日期 2017年5月18日
 * @版本 填写版本
 * @修改历史 新建的时候留空
 */
public enum ErrorCode {
	/**
	 * 成功
	 */
	RISK_SUCESS("200", "操作成功 "),
	/**
     * 成功
     */
    SUCESS("success", "操作成功 "),
	/**
	 * 发生错误
	 */
	ERROR("error", "发生错误"),
	/**
	 * 系统繁忙，请稍后再试
	 */
	SYSTEM_ERROR("system_error", "系统繁忙，请稍后再试"),
	/**
	 * 请求参数非法
	 */
	ILLEGAL_PARAMETERS("illegal_parameters", "请求参数非法"),
	/**
	 * 请求参数不能为空
	 */
	PARAMETERS_NOT_ALLOW_NULL("parameters_not_allow_null", "请求参数不能为空"),
	/**
	 * 缺少必须的参数
	 */
	MISSING_REQUIRED_PARAM("missing_required_param", "缺少必须的参数"),
	/**
	 * 请求方法错误
	 */
	REQUEST_METHOD_WRONG("request method wrong", "请求方法错误"),
	/**
	 * 接口调用错误
	 */
	INTERFACE_CALL_ERROR("interface_call_error", "接口调用错误"),
	/**
	 * 请求内容不能为空
	 */
	MISSING_REQUEST_BODY("missing_request_body", "请求内容不能为空"),
	/**
	 * 对象已存在
	 */
	OBJECT_ALREADY_EXISTS("object_already_exists", "对象已存在"),
	/**
	 * 对象不存在
	 */
	OBJECT_NOT_FOUND("object_not_found", "对象不存在"),

	/**
	 * 没有登录
	 */
	NO_LOGIN("please login", "请登录"),

	/**
	 * 手机号码格式错误
	 */
	FORMAT_WRONG("format_wrong", "电话号码格式错误"),
	/**
	 * token过期
	 */
	TOKEN_INVALID("token_invalid", "token失效"),
	/**
	 * 上传excel内容不能为空
	 */
	EXCEL_EMPTY("excel_empty", "上传excel内容不能为空"),
	    /**
     * 上传excel格式不正确
     */
    EXCEL_INVALID("token_invalid", "上传excel格式不正确"),
	/** 权限不足 **/
	NO_PERMISSION("no_permission", "权限不足"),
	/**
	 * OSS操作失败
	 */
	OSS_FAIL("oss_fail", "oss操作失败");

	private String code;

	private String desc;

	private ErrorCode(final String code, final String desc) {
		this.code = code;
		this.desc = desc;

	}

	/**
	 * 根据code获取desc TODO 增加功能描述
	 * 
	 * @author HOLI
	 * @date Jun 19, 2017
	 * @param code
	 *            code
	 * @return String
	 */
	public static String getDescByCode(final String code) {
		for (final ErrorCode e : ErrorCode.values()) {
			if (e.getCode().equals(code)) {
				return e.desc;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

}
