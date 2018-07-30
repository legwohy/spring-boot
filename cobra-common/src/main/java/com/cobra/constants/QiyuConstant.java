package com.cobra.constants;

/**
 * 七鱼客服相关常数
 * @author admin
 */
public final class QiyuConstant
{
    private QiyuConstant(){throw new AssertionError("No com.cobra.constants.Constant instances for you!");}

    public static final String APP_KEY = "24863dd4e95d6bb05191ec72e1978110";
    public static final String APP_SECRET = "BA8432212FC345A6A84A96D295917434";
    public static final String URL_SEND_MESSAGE = "https://qiyukf.com/openapi/message/send?";
    public static final String URL_APPLY_STAFF = "https://qiyukf.com/openapi/event/applyStaff?";
    public static final String URL_EVALUATE = "https://qiyukf.com/openapi/event/evaluate?";

}
