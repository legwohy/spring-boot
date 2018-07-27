package com.cobra.util;

import com.cobra.constants.TokenConstant;


public final class QiyuUtils
{
    private QiyuUtils(){throw new AssertionError("No com.cobra.util.QiyuUtils instances for you!");}

    public static boolean validate(Long time, String checksum, String content) {
        String md5 = MD5.md5(content);
        String current = QiyuPushCheckSum.encode(TokenConstant.QIYU_APP_SECRET, md5, time);
        return current.equals(checksum);
    }
}
