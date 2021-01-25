/**
 * Copyright &copy; 2012-2014 All rights reserved.
 */

package com.cobra.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类,
 * @see org.apache.commons.lang3.StringUtils
 * @author ThinkGem
 * @version 2013-05-22
 */
public class StringCommonUtils extends org.apache.commons.lang3.StringUtils
{

    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] getBytes(String str)
    {
        if (str != null)
        {
            try
            {
                return str.getBytes(CHARSET_NAME);
            }
            catch (UnsupportedEncodingException e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * 转换为字节数组
     *
     * @return
     */
    public static String toString(byte[] bytes)
    {
        try
        {
            return new String(bytes, CHARSET_NAME);
        }
        catch (UnsupportedEncodingException e)
        {
            return EMPTY;
        }
    }

    public static String toString(Object o)
    {
        return o == null ? "" : o.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs)
    {
        if (str != null)
        {
            for (String s : strs)
            {
                if (str.equals(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html)
    {
        if (isBlank(html))
        {
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
    public static String replaceMobileHtml(String html)
    {
        if (html == null)
        {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }


    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr(String str, int length)
    {
        if (str == null)
        {
            return "";
        }
        try
        {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray())
            {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3)
                {
                    sb.append(c);
                }
                else
                {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static String abbr2(String param, int length)
    {
        if (param == null)
        {
            return "";
        }
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
        for (int i = 0; i < param.length(); i++)
        {
            temp = param.charAt(i);
            if (temp == '<')
            {
                isCode = true;
            }
            else if (temp == '&')
            {
                isHTML = true;
            }
            else if (temp == '>' && isCode)
            {
                n = n - 1;
                isCode = false;
            }
            else if (temp == ';' && isHTML)
            {
                isHTML = false;
            }
            try
            {
                if (!isCode && !isHTML)
                {
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            if (n <= length - 3)
            {
                result.append(temp);
            }
            else
            {
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
        while (m.find())
        {
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHTML.size() - 1; i >= 0; i--)
        {
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }
        return result.toString();
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val)
    {
        if (val == null)
        {
            return 0D;
        }
        try
        {
            return Double.valueOf(trim(val.toString()));
        }
        catch (Exception e)
        {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val)
    {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val)
    {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val)
    {
        return toLong(val).intValue();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
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
    public static String toCapitalizeCamelCase(String s)
    {
        if (s == null)
        {
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
    public static String toUnderScoreCase(String s)
    {
        if (s == null)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1))
            {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c))
            {
                if (!upperCase || !nextUpperCase)
                {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            }
            else
            {
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
    public static void setValueIfNotBlank(String target, String source)
    {
        if (isNotBlank(source))
        {
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
    public static String jsGetVal(String objectString)
    {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (int i = 0; i < vals.length; i++)
        {
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
    public static Object getDefaultValue(Object obj, String defaultValue)
    {
        if (obj == null)
        {
            return defaultValue;
        }
        else
        {
            return obj;
        }
    }

    /**
     * 将Object转换为Integer
     *
     * @param obj
     * @return
     */
    public static Integer getInteger(Object obj)
    {
        Integer o = 0;
        if (obj != null && !"".equals(obj))
        {
            try
            {
                o = Integer.parseInt(obj.toString());
            }
            catch (Exception e)
            {
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
    public static String getString(Object obj)
    {
        String o = "";
        if (obj != null && !"".equals(obj))
        {
            try
            {
                o = obj.toString();
            }
            catch (Exception e)
            {
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
    public static double getDouble(Object obj)
    {
        double o = 0d;
        if (obj != null && !"".equals(obj))
        {
            try
            {
                o = Double.parseDouble(obj.toString());
            }
            catch (Exception e)
            {
            }
        }
        return o;
    }

    public static String getCode(String header, String idstr)
    {
        String tempstr = "";
        int size = 10 - idstr.length();
        for (int i = 0; i < size; i++)
        {
            tempstr += "0";
        }
        return header + tempstr + idstr;
    }

    /**
     * 正在表达式 6-20位字母或数字组成
     * @param file
     * @return
     */
    public static boolean checkPassword(String file, String pattern)
    {
        // 6-20位 字母或数字组成

        if (isNotEmpty(file) && file.matches(pattern))
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
    public static String longToStringPoint(Long dataLong, int bit)
    {
        if (null == dataLong)
        {
            return "";
        }
        // 先转为串型
        String dataStr = dataLong.toString();

        // 相差的位数
        int div = bit + 1 - dataStr.length();
        if (div > 0)
        {
            dataStr = getOffSet(bit, div) + dataStr;
        }

        StringBuilder builder = new StringBuilder(dataStr);
        builder.insert(dataStr.length() - bit, ".");

        return builder.toString();
    }

    private static String getOffSet(int total, int cult)
    {
        String offset = "";
        for (int i = 1; i <= total; i++)
        {
            if (cult == i)
            {
                offset = copyValue(i, "0");
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
    public static String copyValue(int count, String value)
    {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
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
    public static Boolean validateSFZ(String cardNo)
    {
        return Pattern.matches(SFZ_PATTERN, cardNo);
    }

    /**
     * 校验网址
     * @param url
     */
    public static Boolean validateUrl(String url)
    {
        return Pattern.matches(urlPattern, url);
    }

    /**
     * 将字符串的首字符变成大写字符
     *
     * @param s
     * @return
     */
    public static String firstCharUpper(String s)
    {
        String result = s.substring(0, 1).toUpperCase() + s.substring(1);
        return result;
    }

    /**
     * 将字符串的首字符变成小写字符
     *
     * @param s
     * @return
     */
    public static String firstCharLower(String s)
    {
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
    public static String genRandom(int length)
    {
        StringBuffer buffer = new StringBuffer();
        Random r = new Random();
        int i = 0;
        int c;
        while (i < length)
        {
            c = r.nextInt(122);
            if ((48 <= c && c <= 57) || (65 <= c && c <= 90)
                            || (97 <= c && c <= 122))
            {
                buffer.append((char)c);
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
    public static String fillLeft(String orgStr, char fillWith, int fixLen)
    {

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
    public static String fillRight(String orgStr, char fillWith, int fixLen)
    {

        return fillStr(orgStr, fillWith, fixLen, false);

    }

    private static String fillStr(String orgStr, char fillWith, int fixLen,
                    boolean isLeft)
    {

        int toFill = fixLen - orgStr.length();

        if (toFill <= 0)
            return orgStr;

        StringBuilder sb = new StringBuilder(orgStr);
        for (; toFill > 0; toFill--)
        {
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
    public static String toTrim(String str)
    {
        if (str == null)
        {
            return "";
        }
        return str.trim();
    }

    public static String convertToString(int length, int value)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length - ("" + value).length(); i++)
        {
            buffer.append("0");
        }
        buffer.append(value);
        return buffer.toString();
    }

    public static String arrayToString(Object[] array, String split)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++)
        {
            buffer.append(array[i].toString()).append(split);
        }
        if (buffer.length() != 0)
        {
            return buffer.substring(0, buffer.length() - split.length());
        }
        else
        {
            return "";
        }
    }

    @SuppressWarnings("rawtypes")
    public static String arrayToString(Set set, String split)
    {
        StringBuffer buffer = new StringBuffer();
        for (Iterator i = set.iterator(); i.hasNext(); )
        {
            buffer.append(i.next().toString()).append(split);
        }
        if (buffer.length() != 0)
        {
            return buffer.substring(0, buffer.length() - split.length());
        }
        else
        {
            return "";
        }
    }

    public static String trimLeft(String s, char c)
    {
        if (s == null)
        {
            return "";
        }
        else
        {
            StringBuffer b = new StringBuffer();
            char[] cc = s.toCharArray();
            int i = 0;
            for (i = 0; i < cc.length; i++)
            {
                if (cc[i] != c)
                {
                    break;
                }
            }
            for (int n = i; n < cc.length; n++)
            {
                b.append(cc[n]);
            }
            return b.toString();
        }
    }

    public static String repNull(Object o)
    {
        if (o == null)
        {
            return "";
        }
        else
        {
            return o.toString().trim();
        }
    }

    public static String generateRandomString(int len)
    {
        final char[] mm = new char[] {'0', '1', '2', '3', '4', '5', '6', '7',
                        '8', '9'};

        StringBuffer sb = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < len; i++)
        {
            sb.append(mm[random.nextInt(mm.length)]);
        }
        return sb.toString();

    }

    public static String toColumnName(String attributeName)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < attributeName.length(); i++)
        {
            char temp = attributeName.charAt(i);
            if (temp >= 'a' && temp <= 'z')
            {
                buffer.append((char)((int)temp - 32));
            }
            if (temp >= 'A' && temp <= 'Z')
            {
                buffer.append("_").append(temp);
            }
        }
        return buffer.toString();
    }

    public static String toPropertyName(String columnName)
    {
        StringBuffer buffer = new StringBuffer();
        boolean b = false;
        for (int i = 0; i < columnName.length(); i++)
        {
            char temp = columnName.charAt(i);
            if (temp >= '0' && temp <= '9')
            {
                buffer.append((char)((int)temp));
            }
            else if (temp == '_')
            {
                b = true;
            }
            else
            {
                if (!b)
                {
                    buffer.append((char)((int)temp + 32));
                }
                else
                {
                    buffer.append((char)((int)temp));
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

    public static String bytesToHexString(byte[] src)
    {

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
            {
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
    public static byte[] hexStringToBytes(String hexString)
    {
        if (hexString == null || hexString.equals(""))
        {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++)
        {
            int pos = i * 2;
            d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
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

    private static byte charToByte(char c)
    {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

    public static String xor(String s1, String s2)
    {
        int c = Integer.parseInt(s1);
        int d = Integer.parseInt(s2);
        int e = c ^ d;
        String f = Integer.toString(e);
        return f;
    }

    // XSS过滤
    public static boolean CheckXSS(String inputString)
    {

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

        try
        {
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
        }
        catch (Exception e)
        {
            System.err.println("Html2Text: " + e.getMessage());
        }
        if ("-".equals(textStr))
        {
            return true;
        }
        else
        {
            return false;// 返回文本字符串
        }
    }

    public static String converStringToDate(String textStr)
    {
        if (textStr == null)
        {
            return "";
        }
        else
        {
            return textStr.substring(0, 4) + "-" + textStr.substring(4, 6)
                            + "-" + textStr.substring(6, 8) + " "
                            + textStr.substring(8, 10) + ":"
                            + textStr.substring(10, 12) + ":"
                            + textStr.substring(12, 14);
        }
    }

    public static String nvl(String str, String repal)
    {
        if (str == null || str.equals(""))
            return repal;
        return "";
    }

    /**
     * 验证一个字符串是否是数字组成
     *
     * @param s
     *            要验证的字符串
     * @return 如果字符串是数字组成的则返回true, 否则返回false
     */
    public static boolean isNumber(String s)
    {
        if (s == null || s.equals(""))
            return false;
        String num = "0123456789";
        for (int i = 0; i < s.length(); i++)
        {
            if (num.indexOf(s.charAt(i)) < 0)
                return false;
        }
        return true;
    }

    public static int length(String s)
    {
        if (s == null)
        {
            return 0;
        }
        else
        {
            return s.length();
        }
    }

    public static String[] splitToArray(String s, int length)
    {
        if (s == null || s.length() == 0)
        {
            return null;
        }
        int segs = (s.length() - 1) / length + 1;
        String[] arr = new String[segs];
        int i = 0;
        int n = 0;
        StringBuffer b = new StringBuffer();
        while (i < s.length())
        {
            b.append(s.charAt(i));
            i++;
            if (b.length() == length)
            {
                arr[n] = b.toString();
                b = new StringBuffer();
                n++;
            }
        }
        if (b.length() != 0)
        {
            arr[segs - 1] = b.toString();
        }
        return arr;
    }

    public static String replaceString(String oldString, String... arg)
    {
        for (String replaceStr : arg)
        {
            oldString = oldString.replaceFirst("#", replaceStr);

        }
        return oldString;

    }

    public static String formatNumber(double paramDouble, int paramInt)
    {
        return NumberFormat.getNumberInstance().format(
                        round(paramDouble, paramInt));
    }

    public static double round(double paramDouble, int paramInt)
    {
        if (paramInt < 0)
        {
            throw new RuntimeException(
                            "The scale must be a positive integer or zero");
        }
        BigDecimal localBigDecimal1 = new BigDecimal(
                        Double.toString(paramDouble));
        BigDecimal localBigDecimal2 = new BigDecimal("1");
        return localBigDecimal1.divide(localBigDecimal2, paramInt, 4)
                        .doubleValue();
    }

    public static String removeSpaces(String str)
    {
        if (str == null)
        {
            return "";
        }
        String result = str.replaceAll("\\s", "");
        return result;
    }

    /**
     * 前导标识
     */
    public static final int BEFORE = 1;

    /**
     * 后继标识
     */
    public static final int AFTER = 2;

    public static final String DEFAULT_PATH_SEPARATOR = ",";

    /**
     * 将一个中间带逗号分隔符的字符串转换为ArrayList对象
     *
     * @param str 待转换的符串对象
     * @return ArrayList对象
     */
    public static ArrayList<?> strToArrayList(String str)
    {
        return strToArrayListManager(str, DEFAULT_PATH_SEPARATOR);
    }

    /**
     * 将字符串对象按给定的分隔符separator转象为ArrayList对象
     *
     * @param str       待转换的字符串对象
     * @param separator 字符型分隔符
     * @return ArrayList对象
     */
    public static ArrayList<String> strToArrayList(String str, String separator)
    {
        return strToArrayListManager(str, separator);
    }

    /**
     * @param str
     * @param separator
     * @return ArrayList<String>
     * @Description: 分割
     * @Autor: jasonandy@hotmail.com
     */
    private static ArrayList<String> strToArrayListManager(String str, String separator)
    {

        StringTokenizer strTokens = new StringTokenizer(str, separator);
        ArrayList<String> list = new ArrayList<String>();

        while (strTokens.hasMoreTokens())
        {
            list.add(strTokens.nextToken().trim());
        }

        return list;
    }

    /**
     * 将一个中间带逗号分隔符的字符串转换为字符型数组对象
     *
     * @param str 待转换的符串对象
     * @return 字符型数组
     */
    public static String[] strToStrArray(String str)
    {
        return strToStrArrayManager(str, DEFAULT_PATH_SEPARATOR);
    }

    /**
     * 将字符串对象按给定的分隔符separator转象为字符型数组对象
     *
     * @param str       待转换的符串对象
     * @param separator 字符型分隔符
     * @return 字符型数组
     */
    public static String[] strToStrArray(String str, String separator)
    {
        return strToStrArrayManager(str, separator);
    }

    private static String[] strToStrArrayManager(String str, String separator)
    {

        StringTokenizer strTokens = new StringTokenizer(str, separator);
        String[] strArray = new String[strTokens.countTokens()];
        int i = 0;

        while (strTokens.hasMoreTokens())
        {
            strArray[i] = strTokens.nextToken().trim();
            i++;
        }

        return strArray;
    }

    public static Set<String> strToSet(String str)
    {
        return strToSet(str, DEFAULT_PATH_SEPARATOR);
    }

    public static Set<String> strToSet(String str, String separator)
    {
        String[] values = strToStrArray(str, separator);
        Set<String> result = new LinkedHashSet<String>();
        for (int i = 0; i < values.length; i++)
        {
            result.add(values[i]);
        }
        return result;
    }

    /**
     * 将一个数组，用逗号分隔变为一个字符串
     *
     * @param array
     * @return modify by yuce reason:user StringBuffer replace "+"
     */
    public static String ArrayToStr(Object[] array)
    {
        if (array == null || array.length < 0)
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Object obj : array)
        {
            if (StringUtils.isNotBlank(obj.toString()))
            {
                if (sb.length() > 0)
                {
                    sb.append(DEFAULT_PATH_SEPARATOR);
                }
                sb.append(obj.toString());
            }
        }
        return sb.toString();
    }

    public static String CollectionToStr(Collection<String> coll)
    {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (String string : coll)
        {
            if (i > 0)
            {
                sb.append(",");
            }
            i++;
            sb.append(string);
        }
        return sb.toString();
    }

    /**
     * 转换给定字符串的编码格式
     *
     * @param inputString 待转换字符串对象
     * @param inencoding  待转换字符串的编码格式
     * @param outencoding 准备转换为的编码格式
     * @return 指定编码与字符串的字符串对象
     */
    public static String encodingTransfer(String inputString, String inencoding, String outencoding)
    {
        if (inputString == null || inputString.length() == 0)
        {
            return inputString;
        }
        try
        {
            return new String(inputString.getBytes(outencoding), inencoding);
        }
        catch (Exception e)
        {
            return inputString;
        }
    }

    /**
     * 去除左边多余的空格。
     *
     * @param value 待去左边空格的字符串
     * @return 去掉左边空格后的字符串
     */
    public static String trimLeft(String value)
    {
        String result = value;
        if (result == null)
        {
            return result;
        }
        char[] ch = result.toCharArray();
        int index = -1;
        for (int i = 0; i < ch.length; i++)
        {
            if (Character.isWhitespace(ch[i]))
            {
                index = i;
            }
            else
            {
                break;
            }
        }
        if (index != -1)
        {
            result = result.substring(index + 1);
        }
        return result;
    }

    /**
     * 去除右边多余的空格。
     *
     * @param value 待去右边空格的字符串
     * @return 去掉右边空格后的字符串
     */
    public static String trimRight(String value)
    {
        String result = value;
        if (result == null)
        {
            return result;
        }
        char[] ch = result.toCharArray();
        int endIndex = -1;
        for (int i = ch.length - 1; i > -1; i--)
        {
            if (Character.isWhitespace(ch[i]))
            {
                endIndex = i;
            }
            else
            {
                break;
            }
        }
        if (endIndex != -1)
        {
            result = result.substring(0, endIndex);
        }
        return result;
    }

    /**
     * 判断一个字符串中是否包含另一个字符串
     *
     * @param parentStr 父串
     * @param subStr    子串
     * @return 如果父串包含子串的内容返回真，否则返回假
     */
    public static boolean isInclude(String parentStr, String subStr)
    {
        return parentStr.indexOf(subStr) >= 0;
    }

    /**
     * 判断一个字符串中是否包含另一个字符串数组的任何一个
     *
     * @param parentStr 父串
     * @param subStrs   子串集合(以逗号分隔的字符串)
     * @return 如果父串包含子串的内容返回真，否则返回假
     */
    public static boolean isIncludes(String parentStr, String subStrs)
    {
        String[] subStrArray = strToStrArray(subStrs);
        for (int j = 0; j < subStrArray.length; j++)
        {
            String subStr = subStrArray[j];
            if (isInclude(parentStr, subStr))
            {
                return true;
            }
            else
            {
                continue;
            }
        }
        return false;
    }

    /**
     * 判断一个子字符串在父串中重复出现的次数
     *
     * @param parentStr 父串
     * @param subStr    子串
     * @return 子字符串在父字符串中重得出现的次数
     */
    public static int subCount(String parentStr, String subStr)
    {
        int count = 0;

        for (int i = 0; i < (parentStr.length() - subStr.length()); i++)
        {
            String tempString = parentStr.substring(i, i + subStr.length());
            if (subStr.equals(tempString))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * 得到在字符串中从开始字符串到结止字符串中间的子串
     *
     * @param parentStr 父串
     * @param startStr  开始串
     * @param endStr    结止串
     * @return 返回开始串与结止串之间的子串
     */
    public static String subString(String parentStr, String startStr, String endStr)
    {
        return parentStr.substring(parentStr.indexOf(startStr) + startStr.length(), parentStr.indexOf(endStr));
    }

    /**
     * 得到在字符串中从开始字符串到结止字符串中间的子串的集合
     *
     * @param parentStr 父串
     * @param startStr  开始串
     * @param endStr    结止串
     * @return 返回开始串与结止串之间的子串的集合
     */
    public static List<String> subStringList(String parentStr, String startStr, String endStr)
    {
        List<String> result = new ArrayList<String>();
        List<String> elements = dividToList(parentStr, startStr, endStr);
        for (String element : elements)
        {
            if (!isIncludes(element, startStr) || !isIncludes(element, endStr))
            {
                continue;
            }
            result.add(subString(element, startStr, endStr));
        }
        return result;

    }

    /**
     * 将指定的String转换为Unicode的等价值
     * 基础知识： 所有的转义字符都是由 '\' 打头的第二个字符 0-9 :八进制 u :是Unicode转意，长度固定为6位 Other:则为以下字母中的一个 b,t,n,f,r,",\ 都不满足，则产生一个编译错误
     * 提供八进制也是为了和C语言兼容. b,t,n,f,r 则是为控制字符.书上的意思为:描述数据流的发送者希望那些信息如何被格式化或者被表示. 它可以写在代码的任意位置，只要转义后是合法的. 例如: int c=0\u003b
     * 上面的代码可以编译通过，等同于int c=0; \u003b也就是';'的Unicode代码
     *
     * @param inStr 待转换为Unicode的等价字符串
     * @return Unicode的字符串
     */
    public static String getUnicodeStr(String inStr)
    {
        char[] myBuffer = inStr.toCharArray();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < inStr.length(); i++)
        {
            byte b = (byte)myBuffer[i];
            short s = (short)myBuffer[i];
            String hexB = Integer.toHexString(b);
            String hexS = Integer.toHexString(s);
            /*
             * //输出为大写 String hexB = Integer.toHexString(b).toUpperCase(); String hexS =
             * Integer.toHexString(s).toUpperCase(); //print char sb.append("char["); sb.append(i); sb.append("]='");
             * sb.append(myBuffer[i]); sb.append("'\t");
             *
             * //byte value sb.append("byte="); sb.append(b); sb.append(" \\u"); sb.append(hexB); sb.append('\t');
             */

            // short value
            sb.append(" \\u");
            sb.append(fillStr(hexS, "0", 4, AFTER));
            // sb.append('\t');
            // Unicode Block
            // sb.append(Character.UnicodeBlock.of(myBuffer[i]));
        }

        return sb.toString();

    }

    /**
     * 判断一个字符串是否是合法的Java标识符
     *
     * @param s 待判断的字符串
     * @return 如果参数s是一个合法的Java标识符返回真，否则返回假
     */
    public static boolean isJavaIdentifier(String s)
    {
        if (s.length() == 0 || Character.isJavaIdentifierStart(s.charAt(0)))
        {
            return false;
        }
        for (int i = 0; i < s.length(); i++)
            if (!Character.isJavaIdentifierPart(s.charAt(i)))
            {
                return false;
            }
        return true;
    }

    /**
     * 替换字符串中满足条件的字符 例如: replaceAll("\com\hi\platform\base\\util",'\\','/'); 返回的结果为: /com/hi/platform/base/util
     *
     * @param str     待替换的字符串
     * @param oldchar 待替换的字符
     * @param newchar 替换为的字符
     * @return 将str中的所有oldchar字符全部替换为newchar, 并返回这个替换后的字符串
     */
    public static String replaceAll(String str, char oldchar, char newchar)
    {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] == oldchar)
            {
                chars[i] = newchar;
            }
        }
        return new String(chars);
    }

    /**
     * 如果inStr字符串与没有给定length的长度，则用fill填充，在指定direction的方向 如果inStr字符串长度大于length就直截返回inStr，不做处理
     *
     * @param inStr     待处理的字符串
     * @param fill      以该字符串作为填充字符串
     * @param length    填充后要求的长度
     * @param direction 填充方向，如果是AFTER填充在后面，否则填充在前面
     * @return 返回一个指定长度填充后的字符串
     */
    public static String fillStr(String inStr, String fill, int length, int direction)
    {
        if (inStr == null || inStr.length() > length || inStr.length() == 0)
        {
            return inStr;
        }
        StringBuffer tempStr = new StringBuffer("");
        for (int i = 0; i < length - inStr.length(); i++)
        {
            tempStr.append(fill);
        }

        if (direction == AFTER)
        {
            return new String(tempStr.toString() + inStr);
        }
        else
        {
            return new String(inStr + tempStr.toString());
        }
    }

    /**
     * 字符串替换
     *
     * @param str     源字符串
     * @param pattern 待替换的字符串
     * @param replace 替换为的字符串
     * @return
     */
    public static String replace(String str, String pattern, String replace)
    {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0)
        {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));

        return result.toString();
    }

    /**
     * 分隔字符串到一个集合
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static List<String> dividToList(String str, String start, String end)
    {
        if (str == null || str.length() == 0)
        {
            return null;
        }
        int s = 0;
        int e = 0;
        List<String> result = new ArrayList<String>();
        if (isInclude(str, start) && isInclude(str, end))
        {
            while ((e = str.indexOf(start, s)) >= 0)
            {
                result.add(str.substring(s, e));
                s = str.indexOf(end, e) + end.length();
                result.add(str.substring(e, s));
            }
            if (s < str.length())
            {
                result.add(str.substring(s));
            }
            if (s == 0)
            {
                result.add(str);
            }
        }
        else
        {
            result.add(str);
        }
        return result;
    }

    /**
     * 首字母大写
     *
     * @param string
     * @return
     */
    public static String upperFrist(String string)
    {
        if (string == null)
        {
            return null;
        }
        String upper = string.toUpperCase();
        return upper.substring(0, 1) + string.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param string
     * @return
     */
    public static String lowerFrist(String string)
    {
        if (string == null)
        {
            return null;
        }
        String lower = string.toLowerCase();
        return lower.substring(0, 1) + string.substring(1);
    }

    public static String URLEncode(String string, String encode)
    {
        try
        {
            return URLEncoder.encode(string, encode);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将一个日期类型的对象，转换为指定格式的字符串
     *
     * @param date   待转换的日期
     * @param format 转换为字符串的相应格式 例如：DateToStr(new Date() ,"yyyy.MM.dd G 'at' hh:mm:ss a zzz");
     * @return 一个字符串
     * <p>
     * <p>
     * <table border="0" cellspacing="3" cellpadding="0">
     * <tr bgcolor="#ccccff">
     * <th align="left">Letter
     * <th align="left">Date or Time Component
     * <th align="left">Presentation
     * <th align="left">Examples
     * <tr>
     * <td><code>G</code>
     * <td>Era designator
     * <td><a href="#text">Text</a>
     * <td><code>AD</code>
     * <tr bgcolor="#eeeeff">
     * <td>Year
     * <td><a href="#year">Year</a>
     * <td><code>1996</code>; <code>96</code>
     * <tr>
     * <td><code>M</code>
     * <td>Month in year
     * <td><a href="#month">Month</a>
     * <td><code>July</code>; <code>Jul</code>; <code>07</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>w</code>
     * <td>Week in year
     * <td><a href="#number">Number</a>
     * <td><code>27</code>
     * <tr>
     * <td><code>W</code>
     * <td>Week in month
     * <td><a href="#number">Number</a>
     * <td><code>2</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>D</code>
     * <td>Day in year
     * <td><a href="#number">Number</a>
     * <td><code>189</code>
     * <tr>
     * <td><code>d</code>
     * <td>Day in month
     * <td><a href="#number">Number</a>
     * <td><code>10</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>F</code>
     * <td>Day of week in month
     * <td><a href="#number">Number</a>
     * <td><code>2</code>
     * <tr>
     * <td><code>E</code>
     * <td>Day in week
     * <td><a href="#text">Text</a>
     * <td><code>Tuesday</code>; <code>Tue</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>a</code>
     * <td>Am/pm marker
     * <td><a href="#text">Text</a>
     * <td><code>PM</code>
     * <tr>
     * <td><code>H</code>
     * <td>Hour in day (0-23)
     * <td><a href="#number">Number</a>
     * <td><code>0</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>k</code>
     * <td>Hour in day (1-24)
     * <td><a href="#number">Number</a>
     * <td><code>24</code>
     * <tr>
     * <td><code>K</code>
     * <td>Hour in am/pm (0-11)
     * <td><a href="#number">Number</a>
     * <td><code>0</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>h</code>
     * <td>Hour in am/pm (1-12)
     * <td><a href="#number">Number</a>
     * <td><code>12</code>
     * <tr>
     * <td><code>m</code>
     * <td>Minute in hour
     * <td><a href="#number">Number</a>
     * <td><code>30</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>s</code>
     * <td>Second in minute
     * <td><a href="#number">Number</a>
     * <td><code>55</code>
     * <tr>
     * <td><code>S</code>
     * <td>Millisecond
     * <td><a href="#number">Number</a>
     * <td><code>978</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>z</code>
     * <td>Time zone
     * <td><a href="#timezone">General time zone</a>
     * <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code>
     * <tr>
     * <td><code>Z</code>
     * <td>Time zone
     * <td><a href="#rfc822timezone">RFC 822 time zone</a>
     * <td><code>-0800</code>
     * </table>
     */
    public static String DateToStr(Date date, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 对给定的字符串做html转义
     *
     * @param string
     * @return
     */
    public static String escapeHtml(String string)
    {
        if (string == null || string.length() == 0)
        {
            return "";
        }

        char b;
        char c = 0;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);
        String t;

        for (i = 0; i < len; i += 1)
        {
            b = c;
            c = string.charAt(i);
            switch (c)
            {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    if (b == '<')
                    {
                        sb.append('\\');
                    }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100'))
                    {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u" + t.substring(t.length() - 4));
                    }
                    else
                    {
                        sb.append(c);
                    }
            }
        }

        return sb.toString();
    }

    /**
     * 生成一个指定长度的随机字符串
     *
     * @param length 返回的字符串长度
     * @return 返回一个随机
     */
    public static String randomString(int length)
    {
        if (length < 1)
        {
            return null;
        }
        Random randGen = new Random();
        char[] numbersAndLetters = ("abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++)
        {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(51)];
        }
        return new String(randBuffer);
    }

    public final static String trim(String target)
    {
        if (target == null)
            return null;

        target = target.trim();

        return "".equals(target) ? null : target;
    }

    /**
     * 判断null 或 空字符串
     *
     * @param str
     * @return
     */
    public static boolean isNullOrBlank(String str)
    {
        if (str == null)
        {
            return true;
        }
        str = str.trim();
        if (!str.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * 判断 空字符串
     *
     * @param value
     * @return
     */
    public static boolean isBlank(String value)
    {
        boolean ret = false;
        if ((value != null) && (value.equals("")))
        {
            ret = true;
        }
        return ret;
    }

    /**
     * 判断null
     */
    public static boolean isNull(Object value)
    {
        return (value == null);
    }

    /**
     * Title : 获取随机数
     *
     * @param digits 随机数取值范围 默认为0123456789
     * @param length 随机数位数 默认为1位
     * @return
     * @author : 聂水根
     */
    public static String getRandom(String digits, int length)
    {
        if (null == digits)
        {
            digits = "0123456789";
        }
        if (length <= 0)
        {
            length = 1;
        }
        char[] code = new char[length];
        String temp = "";
        for (int i = 0; i < length; i++)
        {
            code[i] = digits.charAt((int)(Math.random() * digits.length()));
            temp += "0";
        }
        String verifyCode = new String(code);

        if (verifyCode.equals(temp))
        {
            verifyCode = getRandom(digits, length);
        }
        return verifyCode;
    }

    /**
     * Title : 获取范围在min到max间的随机数
     *
     * @param min
     * @param max
     * @return
     * @author : 聂水根
     */
    public static int getRandom(int min, int max)
    {
        return (int)(Math.random() * (max - min) + min);
    }

    /**
     * Title : 获取范围在min到max间的num个随机数
     *
     * @param min
     * @param max
     * @return
     * @author : 聂水根
     */
    public static Integer[] getRandomNum(int min, int max, int num)
    {
        Integer[] result = new Integer[num];
        for (int i = 0; i < num; i++)
        {
            result[i] = getRandom(min, max);
        }

        for (int i = 0; i < num; i++)
        {
            for (int k = i + 1; k < num; k++)
            {
                if (result[i] == result[k])
                {
                    getRandomNum(min, max, num);
                }
            }
        }
        return result;
    }

    public static String ifBlank(String... ss)
    {
        String ret = "";
        for (String s : ss)
        {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(s))
            {
                ret = s;
                break;
            }
        }
        return ret;
    }

    public static String getUrlParamters(String url, String prefix)
    {
        String _return = null;
        if (url.indexOf("?") >= 0)
        {
            url = url.substring(url.indexOf("?") + 1);
            String[] paramters = url.split("&");
            if (paramters != null)
            {
                for (String s : paramters)
                {
                    if (s.startsWith(prefix))
                    {
                        String[] _temp = s.split("=");
                        if (_temp.length > 1)
                        {
                            _return = _temp[1];
                        }
                    }
                }
            }
        }
        return _return;

    }

    /**
     * 字符串转换成Long型数字
     *
     * @param src
     * @return
     */
    public static long toLong(String src)
    {
        long dest = 0;
        if (src == null || src.trim().equals(""))
        {
            return 0;
        }

        try
        {
            dest = Long.parseLong(src.trim());
        }
        catch (Exception e)
        {
        }
        return dest;
    }

    /**
     * 创建*Info类型的字符串，一般用于信息查询类接口的返回。
     * <p>
     * 例如，调用buildInfoString("#",aaa,bbb,null,ddd); 得到字符串：aaa#bbb##ddd
     *
     * @param delimiter
     * @param items
     * @return
     */
    public static String buildInfoString(String delimiter, Object... items)
    {
        StringBuffer buff = new StringBuffer();
        boolean firstItem = true;
        for (Object item : items)
        {
            if (firstItem)
            {
                firstItem = false;
            }
            else
            {
                buff.append(delimiter);
            }

            if (item == null)
            {
                buff.append("");
            }
            else
            {
                buff.append(item.toString());
            }

        }

        return buff.toString();
    }

    public static long yuan2Cent(String src)
    {
        if (src == null || src.trim().equals(""))
        {
            return 0;
        }

        long ret = 0;
        try
        {
            ret = (long)(Math.round(Double.parseDouble(src) * 100));
        }
        catch (Exception e)
        {
        }

        return ret;
    }

    /**
     * 把分转换为元
     *
     * @param src
     * @param floor 是否取整
     * @return
     */
    public static String cent2Yuan(String src, boolean floor)
    {

        long temp = 0;
        try
        {
            String tem = src.trim();
            temp = Long.parseLong(tem);
        }
        catch (Exception e)
        {
        }
        return cent2Yuan(temp, floor);
    }

    public static String cent2Yuan(long src, boolean floor)
    {
        DecimalFormat SDF_YUAN = new DecimalFormat("0.00");
        String yuan = "0.00";
        try
        {
            yuan = SDF_YUAN.format(src / 100.00);
        }
        catch (Exception e)
        {
        }

        if (floor)
        {
            int idxDot = yuan.indexOf(".");
            if (idxDot >= 0)
            {
                yuan = yuan.substring(0, idxDot);
            }
        }
        return yuan;

    }

    public static String cent2Yuan(Double src, boolean floor)
    {
        DecimalFormat SDF_YUAN = new DecimalFormat("0.00");
        String yuan = "0.00";
        try
        {
            yuan = SDF_YUAN.format(src / 100.00);
        }
        catch (Exception e)
        {
        }

        if (floor)
        {
            int idxDot = yuan.indexOf(".");
            if (idxDot >= 0)
            {
                yuan = yuan.substring(0, idxDot);
            }
        }
        return yuan;

    }

    /**
     * 将一个字符串按分隔符分成多个子串。 此方法用于代替Jdk的String.split()方法，不同的地方：<br>
     * (1)在此方法中，空字符串不管在哪个位置都视为一个有效字串。 <br>
     * (2)在此方法中，分隔符参数不是一个正则表达式。<br>
     *
     * @param src       源字符串
     * @param delimiter 分隔符
     * @return 字符串数组。
     */
    public static String[] split(String src, String delimiter)
    {
        if (src == null || delimiter == null || src.trim().equals("") || delimiter.equals(""))
        {
            return new String[] {src};
        }

        ArrayList<String> list = new ArrayList<String>();

        int lengthOfDelimiter = delimiter.length();
        int pos = 0;
        while (true)
        {
            if (pos < src.length())
            {

                int indexOfDelimiter = src.indexOf(delimiter, pos);
                if (indexOfDelimiter < 0)
                {
                    list.add(src.substring(pos));
                    break;
                }
                else
                {
                    list.add(src.substring(pos, indexOfDelimiter));
                    pos = indexOfDelimiter + lengthOfDelimiter;
                }
            }
            else if (pos == src.length())
            {
                list.add("");
                break;
            }
            else
            {
                break;
            }
        }

        String[] result = new String[list.size()];
        list.toArray(result);
        return result;

    }

    /**
     * 若原对象为Null则返回空字符串,否则先转为String类型，再剪去字符串两端的空格及控制字符
     *
     * @param src
     * @return
     */
    public static String trim(Object src)
    {
        if (src == null)
        {
            return "";
        }

        String str = src.toString();
        return str.trim();
    }

    /**
     * 填充数字0
     *
     * @param src
     * @param targetLength
     * @return
     */
    public static String fill(long src, int targetLength)
    {
        return fill(String.valueOf(src), "0", targetLength, true);
    }

    /**
     * 填充字符串。如果原字符串比目标长度长，则截去多出的部分。如果onTheLeft等于true 截去左边部分，否则截去右边部分。 注意填充物一般为单个字符，如果是多个字符，可能导致最后的结果不可用。
     *
     * @param src          原字符串
     * @param padding      填充物，一般是0、空格等
     * @param targetLength 目标长度
     * @param onTheLeft    是否左填充。
     * @return
     */
    public static String fill(String src, String padding, int targetLength, boolean onTheLeft)
    {

        if (src == null)
        {
            src = "";
        }

        while (src.length() < targetLength)
        {
            if (onTheLeft)
            {
                src = padding + src;
            }
            else
            {
                src = src + padding;
            }
        }

        if (src.length() > targetLength)
        {
            if (onTheLeft)
            {
                src = src.substring(src.length() - targetLength);
            }
            else
            {
                src = src.substring(0, targetLength);
            }
        }

        return src;
    }

    public static String changeListToString(List<String> source, String delimiter)
    {
        StringBuilder builder = new StringBuilder();
        if (source != null && source.size() > 0)
        {
            int len = source.size();
            for (int i = 0; i < len; i++)
            {
                builder.append(source.get(i));
                if (i < len - 1)
                {
                    builder.append(delimiter);
                }

            }
        }
        return builder.toString();
    }

    public static String changeListToStringWithTag(List<String> source, String delimiter, String tag)
    {
        StringBuilder builder = new StringBuilder();
        if (source != null && source.size() > 0)
        {
            int len = source.size();
            for (int i = 0; i < len; i++)
            {
                builder.append(tag + source.get(i) + tag);
                if (i < len - 1)
                {
                    builder.append(delimiter);
                }

            }
        }
        return builder.toString();
    }

    /**
     * 是否存在null、或者空字符串。任意一个参数满足条件，返回true；否则返回false。<br>
     * 先将参数对象转成字符串，修剪后进行判断。仅包含空格或ASCII控制字符也视为条件满足。<br>
     * <p>
     * Noe:Null Or Empty<br>
     *
     * @return
     */
    public static boolean existNoe(Object... someObj)
    {
        if (someObj == null || someObj.length == 0)
        {
            throw new RuntimeException("参数不能为空,必须有至少一个对象");
        }

        for (int i = 0; i < someObj.length; i++)
        {
            Object obj = someObj[i];
            if (obj == null || obj.toString().trim().equals(""))
            {
                return true;
            }
        }

        return false;

    }

    /**
     * 若原字符串为Null则返回空字符串。
     *
     * @param src
     * @return
     */
    public static String null2Empty(String src)
    {
        if (src == null)
        {
            return "";
        }
        return src;
    }

    /**
     * 若原字符串为Null则返回空字符串。
     *
     * @param src
     * @return
     */
    public static boolean isEmpty(String src)
    {
        String value = null2Empty(src);
        if ("".equals(value))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 是否全部非空
     *
     * @param src
     * @return
     */
    public static boolean isAllNotEmpty(String... src)
    {
        for (int i = 0; i < src.length; i++)
        {
            String value = src[i];
            if (value == null || value.equals(""))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * 是否全部为空
     *
     * @param src
     * @return
     */
    public static boolean isAllEmpty(String... src)
    {
        for (int i = 0; i < src.length; i++)
        {
            String value = src[i];
            if (value != null && !value.equals(""))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * 是否全为字母或数字
     *
     * @param src
     * @return
     */
    public static boolean isLetterOrNumber(String src)
    {
        if (src == null)
        {
            return false;
        }

        try
        {
            byte[] bytesOfSrc = src.getBytes("utf-8");

            for (int i = 0; i < bytesOfSrc.length; i++)
            {
                byte one = bytesOfSrc[i];
                if (one < '0' || (one > '9' && one < 'A') || (one > 'Z' && one < 'a') || one > 'z')
                {
                    return false;
                }
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * 比较两个字符串是否相等
     *
     * @param one
     * @param another
     * @return
     */
    public static boolean equals(String one, String another)
    {
        if (one == null)
        {
            if (another == null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (another == null)
            {
                return false;
            }
            else
            {
                return one.equals(another);
            }
        }
    }

    /**
     * 获取字符串
     *
     * @param value  字符串
     * @param maxLen 最大字符串长度
     * @return
     */
    public static String getSubStr(String value, int maxLen)
    {
        if (value.length() > maxLen)
        {
            value = value.substring(0, maxLen);
        }
        return value;
    }

    public static String takeFromStr(String content, String regex)
    {
        Matcher matcher = Pattern.compile(regex).matcher(content);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * 提取数字
     * @param content
     * @return
     */
    public static Integer takeNumberFromStr(String content)
    {
        String regex = "\\D+(\\d+)";
        String result = takeFromStr(content, regex);
        if (isEmpty(result))
        {
            return 0;
        }
        return Integer.valueOf(result);
    }

    /**
     * 截取字符长度  长度低 末尾补齐
     * @param content
     * @param length
     * @return
     */
    public static String sub(String content,int length){
        if(content.length()<length){

            StringBuilder sb = new StringBuilder(content);
            String suffix = content.substring(content.length()-1,content.length());
            for (int i = 0;i<length-content.length();i++){
                sb.append(suffix);
            }
            return sb.toString();
        }else {
            return content.substring(0,length);
        }
    }

    /**
     * 自定义填充
     * @param sSrc
     * @param blockSize
     * @return
     */
    public static String padding(String sSrc, int blockSize){
        byte[] dataBytes = sSrc.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0)
        {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

        return new String(plaintext);
    }
    public static byte[] padding(String sSrc, Cipher cipher)throws Exception{
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = sSrc.getBytes(CHARSET_NAME);
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0)
        {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

        return plaintext;
    }



    public static void main(String[] args)
    {
        String value = "1234";
        System.out.println(sub(value,4));

    }



}
