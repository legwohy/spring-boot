/**
 * Copyright &copy; 2012-2014 All rights reserved.
 */
package com.cobra.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类,
 * @see org.apache.commons.lang3.StringUtils
 * @author ThinkGem
 * @version 2013-05-22
 */
public class StringCommonUtils extends org.apache.commons.lang3.StringUtils {

    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 转换为字节数组
     *
     * @return
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }


    public static String toString(Object o) {
        return o == null ? "" : o.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html
     * @return
     */
    public static String replaceMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param txt
     * @return
     */
    public static String toHtml(String txt) {
        if (txt == null) {
            return "";
        }
        return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
    }

    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String abbr2(String param, int length) {
        if (param == null) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHTML = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHTML) {
                isHTML = false;
            }
            try {
                if (!isCode && !isHTML) {
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (n <= length - 3) {
                result.append(temp);
            } else {
                result.append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去掉不需要结素标记的HTML标记
        temp_result = temp_result.replaceAll(
                "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
                "");
        // 去掉成对的HTML标记
        temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
        // 用正则表达式取出标记
        Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
        Matcher m = p.matcher(temp_result);
        List<String> endHTML = Lists.newArrayList();
        while (m.find()) {
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHTML.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }
        return result.toString();
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(trim(val.toString()));
        } catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }




    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 如果不为空，则设置值
     *
     * @param target
     * @param source
     */
    public static void setValueIfNotBlank(String target, String source) {
        if (isNotBlank(source)) {
            target = source;
        }
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串
     *                     例如：row.user.id
     *                     返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (int i = 0; i < vals.length; i++) {
            val.append("." + vals[i]);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }

    /**
     * 判断对象为空，返回默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static Object getDefaultValue(Object obj, String defaultValue) {
        if (obj == null) {
            return defaultValue;
        } else {
            return obj;
        }
    }

    /**
     * 将Object转换为Integer
     *
     * @param obj
     * @return
     */
    public static Integer getInteger(Object obj) {
        Integer o = 0;
        if (obj != null && !"".equals(obj)) {
            try {
                o = Integer.parseInt(obj.toString());
            } catch (Exception e) {
            }
        }
        return o;
    }

    /**
     * 将Object转换为String
     *
     * @param obj
     * @return
     */
    public static String getString(Object obj) {
        String o = "";
        if (obj != null && !"".equals(obj)) {
            try {
                o = obj.toString();
            } catch (Exception e) {
            }
        }
        return o;
    }

    /**
     * 将Object转换为Double
     *
     * @param obj
     * @return
     */
    public static double getDouble(Object obj) {
        double o = 0d;
        if (obj != null && !"".equals(obj)) {
            try {
                o = Double.parseDouble(obj.toString());
            } catch (Exception e) {
            }
        }
        return o;
    }

    public static String getCode(String header, String idstr) {
        String tempstr = "";
        int size = 10 - idstr.length();
        for (int i = 0; i < size; i++) {
            tempstr += "0";
        }
        return header + tempstr + idstr;
    }

    /**
     * 正在表达式 6-20位字母或数字组成
     * @param file
     * @return
     */
    public static boolean checkPassword(String file,String pattern)
    {
        // 6-20位 字母或数字组成

        if(!isEmpty(file) && file.matches(pattern))
        {
            return true;
        }

        return false;
    }

    /**
     * Long转换为带小数点的数
     * @param dataLong Long整形的数字
     * @return
     */
    public static String longToStringPoint(Long dataLong,int bit) {
        if (null == dataLong) {
            return "";
        }
        // 先转为串型
        String dataStr = dataLong.toString();

        // 相差的位数
        int div = bit + 1 - dataStr.length();
        if (div > 0) {
            dataStr = getOffSet(bit,div) + dataStr;
        }

        StringBuilder builder = new StringBuilder(dataStr);
        builder.insert(dataStr.length() - bit, ".");

        return builder.toString();
    }
    private static String getOffSet(int total,int cult){
        String offset = "";
        for (int i = 1;i<=total;i++){
            if(cult == i){
                offset = copyValue(i,"0");
                break;
            }
        }

        return offset;
    }
    /**
     * 指数级增长
     * @param count
     * @param value
     * @return
     */
    public static String copyValue(int count,String value){

        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < count;i++){
            builder = builder.append(value);
        }

        return builder.toString();
    }

    /**
     * 网址
     */
    public final static String urlPattern = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    /**
     * 身份证
     * */
    public final static String SFZ_PATTERN = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";


    /**
     * 校验身份证
     * @param cardNo
     */
    public static Boolean validateSFZ(String cardNo){
        return Pattern.matches(SFZ_PATTERN,cardNo);
    }

    /**
     * 校验网址
     * @param url
     */
    public static Boolean validateUrl(String url){
        return Pattern.matches(urlPattern,url);
    }

    /**
     * 将字符串的首字符变成大写字符
     *
     * @param s
     * @return
     */
    public static String firstCharUpper(String s) {
        String result = s.substring(0, 1).toUpperCase() + s.substring(1);
        return result;
    }

    /**
     * 将字符串的首字符变成小写字符
     *
     * @param s
     * @return
     */
    public static String firstCharLower(String s) {
        String result = s.substring(0, 1).toLowerCase() + s.substring(1);
        return result;
    }

    /**
     * 生成指定长度的随机字符串（0--9,A--Z,a--Z）
     *
     *
     *
     *
     * @param length
     * @return
     */
    public static String genRandom(int length) {
        StringBuffer buffer = new StringBuffer();
        Random r = new Random();
        int i = 0;
        int c;
        while (i < length) {
            c = r.nextInt(122);
            if ((48 <= c && c <= 57) || (65 <= c && c <= 90)
                    || (97 <= c && c <= 122)) {
                buffer.append((char) c);
                i++;
            }
        }
        return buffer.toString();
    }

    /**
     * 字符串左边补零
     *
     *
     *
     *
     * 例：将字符串"ABC"用"0"补足8位，结果为"00000ABC"
     *
     * @param orgStr
     *            原始字符串
     *
     *
     *
     *
     * @param fillWith
     *            用来填充的字符
     *
     *
     *
     *
     * @param fixLen
     *            固定长度
     * @return
     */
    public static String fillLeft(String orgStr, char fillWith, int fixLen) {

        return fillStr(orgStr, fillWith, fixLen, true);

    }

    /**
     * 字符串右边补零
     *
     *
     *
     *
     *
     * @param orgStr
     * @param fillWith
     * @param fixLen
     * @return
     */
    public static String fillRight(String orgStr, char fillWith, int fixLen) {

        return fillStr(orgStr, fillWith, fixLen, false);

    }

    private static String fillStr(String orgStr, char fillWith, int fixLen,
                                  boolean isLeft) {

        int toFill = fixLen - orgStr.length();

        if (toFill <= 0)
            return orgStr;

        StringBuilder sb = new StringBuilder(orgStr);
        for (; toFill > 0; toFill--) {
            if (isLeft)
                sb.insert(0, fillWith);
            else
                sb.append(fillWith);
        }

        return sb.toString();

    }

    /**
     * toTrim
     *
     * @param str
     * @return
     */
    public static String toTrim(String str) {
        if (str == null) {
            return "";
        }
        return str.trim();
    }

    public static String convertToString(int length, int value) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length - ("" + value).length(); i++) {
            buffer.append("0");
        }
        buffer.append(value);
        return buffer.toString();
    }

    public static String arrayToString(Object[] array, String split) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append(array[i].toString()).append(split);
        }
        if (buffer.length() != 0) {
            return buffer.substring(0, buffer.length() - split.length());
        } else {
            return "";
        }
    }

    @SuppressWarnings("rawtypes")
    public static String arrayToString(Set set, String split) {
        StringBuffer buffer = new StringBuffer();
        for (Iterator i = set.iterator(); i.hasNext();) {
            buffer.append(i.next().toString()).append(split);
        }
        if (buffer.length() != 0) {
            return buffer.substring(0, buffer.length() - split.length());
        } else {
            return "";
        }
    }

    public static String trimLeft(String s, char c) {
        if (s == null) {
            return "";
        } else {
            StringBuffer b = new StringBuffer();
            char[] cc = s.toCharArray();
            int i = 0;
            for (i = 0; i < cc.length; i++) {
                if (cc[i] != c) {
                    break;
                }
            }
            for (int n = i; n < cc.length; n++) {
                b.append(cc[n]);
            }
            return b.toString();
        }
    }

    public static String repNull(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString().trim();
        }
    }

    public static String generateRandomString(int len) {
        final char[] mm = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9' };

        StringBuffer sb = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            sb.append(mm[random.nextInt(mm.length)]);
        }
        return sb.toString();

    }

    public static String toColumnName(String attributeName) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < attributeName.length(); i++) {
            char temp = attributeName.charAt(i);
            if (temp >= 'a' && temp <= 'z') {
                buffer.append((char) ((int) temp - 32));
            }
            if (temp >= 'A' && temp <= 'Z') {
                buffer.append("_").append(temp);
            }
        }
        return buffer.toString();
    }

    public static String toPropertyName(String columnName) {
        StringBuffer buffer = new StringBuffer();
        boolean b = false;
        for (int i = 0; i < columnName.length(); i++) {
            char temp = columnName.charAt(i);
            if (temp >= '0' && temp <= '9') {
                buffer.append((char) ((int) temp));
            } else if (temp == '_') {
                b = true;
            } else {
                if (!b) {
                    buffer.append((char) ((int) temp + 32));
                } else {
                    buffer.append((char) ((int) temp));
                }
                b = false;
            }
        }
        return buffer.toString();
    }

    /**
     *
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src
     *            byte[] data
     * @return hex string
     */

    public static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();

    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString
     *            the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c
     *            char
     * @return byte
     */

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String xor(String s1, String s2) {
        int c = Integer.parseInt(s1);
        int d = Integer.parseInt(s2);
        int e = c ^ d;
        String f = Integer.toString(e);
        return f;
    }

    // XSS过滤
    public static boolean CheckXSS(String inputString) {

        String htmlStr = inputString; // 含html标签的字符串

        String textStr = "";
        java.util.regex.Pattern p_script;

        java.util.regex.Matcher m_script;

        java.util.regex.Pattern p_style;

        java.util.regex.Matcher m_style;

        java.util.regex.Pattern p_iframe;

        java.util.regex.Matcher m_iframe;

        java.util.regex.Pattern p_img;

        java.util.regex.Matcher m_img;

        java.util.regex.Pattern p_image;

        java.util.regex.Matcher m_image;

        java.util.regex.Pattern p_html;

        java.util.regex.Matcher m_html;

        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

            // 定义iframe的正则表达式{或<iframe[^>]*?>[\\s\\S]*?<\\/iframe> }
            String regEx_iframe = "<[\\s]*?iframe[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?iframe[\\s]*?>";

            // 定义img的正则表达式{或<img[^>]*?>[\\s\\S]*?<\\/img> }
            String regEx_img = "<[\\s]*?img[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?img[\\s]*?>";

            // 定义image的正则表达式{或<image[^>]*?>[\\s\\S]*?<\\/image> }
            String regEx_image = "<[\\s]*?image[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?image[\\s]*?>";

            // 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll("-"); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll("-"); // 过滤style标签

            p_iframe = Pattern.compile(regEx_iframe, Pattern.CASE_INSENSITIVE);
            m_iframe = p_iframe.matcher(htmlStr);
            htmlStr = m_iframe.replaceAll("-"); // 过滤style标签

            p_img = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            m_img = p_img.matcher(htmlStr);
            htmlStr = m_img.replaceAll("-"); // 过滤img标签

            p_image = Pattern.compile(regEx_image, Pattern.CASE_INSENSITIVE);
            m_image = p_image.matcher(htmlStr);
            htmlStr = m_image.replaceAll("-"); // 过滤image标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll("-"); // 过滤html标签

            textStr = htmlStr;
        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        if ("-".equals(textStr)) {
            return true;
        } else {
            return false;// 返回文本字符串
        }
    }

    public static String converStringToDate(String textStr) {
        if (textStr == null) {
            return "";
        } else {
            return textStr.substring(0, 4) + "-" + textStr.substring(4, 6)
                    + "-" + textStr.substring(6, 8) + " "
                    + textStr.substring(8, 10) + ":"
                    + textStr.substring(10, 12) + ":"
                    + textStr.substring(12, 14);
        }
    }

    public static String nvl(String str, String repal) {
        if (str == null || str.equals(""))
            return repal;
        return "";
    }

    /**
     * 验证一个字符串是否是数字组成
     *
     * @param s
     *            要验证的字符串
     * @return 如果字符串是数字组成的则返回true,否则返回false
     */
    public static boolean isNumber(String s) {
        if (s == null || s.equals(""))
            return false;
        String num = "0123456789";
        for (int i = 0; i < s.length(); i++) {
            if (num.indexOf(s.charAt(i)) < 0)
                return false;
        }
        return true;
    }

    public static int length(String s) {
        if (s == null) {
            return 0;
        } else {
            return s.length();
        }
    }

    public static String[] splitToArray(String s, int length) {
        if (s == null || s.length() == 0) {
            return null;
        }
        int segs = (s.length() - 1) / length + 1;
        String[] arr = new String[segs];
        int i = 0;
        int n = 0;
        StringBuffer b = new StringBuffer();
        while (i < s.length()) {
            b.append(s.charAt(i));
            i++;
            if (b.length() == length) {
                arr[n] = b.toString();
                b = new StringBuffer();
                n++;
            }
        }
        if (b.length() != 0) {
            arr[segs - 1] = b.toString();
        }
        return arr;
    }

    public static boolean isNotEmpty(String str) {
        return !Strings.isNullOrEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return Strings.isNullOrEmpty(str);
    }

    public static String replaceString(String oldString, String... arg) {
        for (String replaceStr : arg) {
            oldString = oldString.replaceFirst("#", replaceStr);

        }
        return oldString;

    }

    public static String formatNumber(double paramDouble, int paramInt) {
        return NumberFormat.getNumberInstance().format(
                round(paramDouble, paramInt));
    }

    public static double round(double paramDouble, int paramInt) {
        if (paramInt < 0) {
            throw new RuntimeException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal localBigDecimal1 = new BigDecimal(
                Double.toString(paramDouble));
        BigDecimal localBigDecimal2 = new BigDecimal("1");
        return localBigDecimal1.divide(localBigDecimal2, paramInt, 4)
                .doubleValue();
    }

    public static String removeSpaces(String str){
        if(str==null){
            return "";
        }
        String result=str.replaceAll("\\s", "");
        return result;
    }


}
