
package com.cobra.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.apache.commons.lang.StringUtils.isEmpty;


public class DateUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
    /**
     * 日期格式
     */
    protected static final String DAY_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtils()
    {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 日期转换成字符串格式
     * @author HOLI
     * @date Jun 19, 2017
     * @param date date
     * @return String
     */
    public static String dateToString(Date date)
    {
        return dateToString(date, null);
    }

    /**
     * 字符串转车成对应的格式
     * @author HOLI
     * @date Jun 19, 2017
     * @param date date
     * @param format 格式
     * @return String
     */
    public static String dateToString(Date date, String format)
    {
        String str = null;
        DateFormat sdf;
        if (StringUtils.isEmpty(format))
        {
            sdf = new SimpleDateFormat(DAY_FORMAT);
        }
        else
        {
            sdf = new SimpleDateFormat(format);
        }
        if (date != null)
        {
            str = sdf.format(date);
        }
        return str;
    }

    /**
     * 转换日期格式
     * @author HOLI
     * @date Jun 21, 2017
     * @param date date
     * @return Date
     */
    public static Date dateToDate(Date date)
    {
        return dateToDate(date, null);
    }

    /**
     * 
     * @author xingyu.wu
     * @date 2017年5月18日
     * @param date date
     * @param format format
     * @return Date
     */
    public static Date dateToDate(Date date, String format)
    {
        Date newDate = null;
        SimpleDateFormat df;
        if (StringUtils.isEmpty(format))
        {
            df = new SimpleDateFormat(DAY_FORMAT);
        }
        else
        {
            df = new SimpleDateFormat(format);
        }
        String str = df.format(date);
        try
        {
            newDate = df.parse(str);
        }
        catch (ParseException e)
        {
            LOGGER.error("Exception e" + e);
        }
        return newDate;
    }

    /**
     * 
     * @author xingyu.wu
     * @date 2017年5月18日
     * @param source source
     * @param format format
     * @return Date
     */
    public static Date getStringToDate(String source, String format)
    {
        Date date = null;
        format = isEmpty(format) ? DAY_FORMAT : format;
        SimpleDateFormat df = new SimpleDateFormat(format);
        try
        {
            date = df.parse(source);
        }
        catch (ParseException e)
        {
            LOGGER.error("Exception e" + e);
        }
        return date;
    }

    public static Date getStringToDate(String source)
    {
        return getStringToDate(source, null);
    }

    public static String StringToString(String source, String format)
    {
        Date date = getStringToDate(source, format);
        return dateToString(date, format);
    }

    /**
     * 获取当前网络时间
     * @author lihong
     * @date 2017年8月8日
     * @return
     */
    public static long getCurrentTimeMillis()
    {
        //        try
        //        {
        //            URL url = new URL("http://www.taobao.com");//取得资源对象
        //            URLConnection uc = url.openConnection();//生成连接对象
        //            uc.connect();//发出连接
        //            long ld = uc.getDate();//读取网站日期时间
        //            return ld;
        //        }
        //        catch (MalformedURLException e)
        //        {
        //            e.printStackTrace();
        //        }
        //        catch (IOException e)
        //        {
        //            e.printStackTrace();
        //        }
        return System.currentTimeMillis();
    }

    /**
     * 
     * @author wxy
     * @date 2017年8月24日
     * @param soure  当前时间
     * @param direction 过去时间
     * @return
     */
    public static int distanceDays(long source, long direction)
    {
        Long days = (source - direction) / (1000 * 3600 * 24);
        return days.intValue();
    }

    /**
     * 获取几天前的日期
     * @author lihong
     * @date 2017年8月25日
     * @param days
     * @return
     */
    public static Date getDateForDayBefor(int days)
    {
        Calendar calendar = Calendar.getInstance();
        long time = getCurrentTimeMillis();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        Date lastDate = new Date(calendar.getTimeInMillis());
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            return sdf.parse(sdf.format(lastDate));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *           
     * @author
     * @date 2017年9月6日
     * @param date
     * @param minute
     * @return
     */
    public static Date addMinute(Date date, int minute)
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 返回的就是UTC时间
     */
    public static long getUTCTime(){
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 把当前日期格式化成yyyy-MM-dd
     * @return
     */
    public static String format(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String time = df.format(date);
        return time;
    }

}
