package com.cobra.constants;


/**
 * 全局常量
 *
 * @author admin
 */
public final class Constant
{
    private Constant(){throw new AssertionError("No com.cobra.constants.Constant instances for you!");}

    /**
     * 校验token字段名
     */
    public static String token = "AT-HMX-TNT-RDX";

    /**
     * json解析中的code
     */
    public static String code = "code";

    /**
     * json解析中的message
     */
    public static String message = "message";

    public static String LOG_TRACE_ID = "traceLogId";

}
