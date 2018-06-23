
package com.ihome.util;

import com.ihome.exception.ServiceException;
import org.apache.commons.lang.RandomStringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 自定义UUID生成ID 功能描述: TODO 增加描述代码功能
 * 
 * @逻辑说明: TODO 增加描述代码逻辑
 * 
 * @牵涉到的配置项: TODO 若果代码中逻辑牵涉到配置项在这里列出
 *
 * @编码实现人员 HOLI
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 Jun 19, 2017
 * @版本 TODO 填写版本
 * @修改历史 TODO 新建的时候留空
 */
public class UUidGenIdUtils {

	/**
	 * 所有数字加大小写英文字母
	 */
	public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**
	 * 所有大小写英文字母
	 */
	public static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**
	 * 所有数字
	 */
	public static final String NUMBER_CHAR = "0123456789";

	private UUidGenIdUtils() {

	}

	/**
	 * 
	 * TODO 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * 
	 * @author HOLI
	 * @date 2017年5月18日
	 * @return 随机字符串
	 */
	public static String generateString() {
		final int size = 15;
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i <= size; i++) {
			sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * getDateString TODO 增加功能描述
	 * 
	 * @author HOLI
	 * @date Jun 20, 2017
	 * @return String
	 */
	public static String getDateString() {
		final int start = 3;
		final int end = 14;
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yMMddHHmmssSSS");
		String reTime = format.format(date);
		StringBuilder sb = new StringBuilder(reTime);
		return sb.substring(start, end);
	}

	/**
	 * 
	 * TODO 返回一个定长的随机纯字母字符串(只包含大小写字母)
	 * 
	 * @author HOLI
	 * @date 2017年5月18日
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateMixString(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALL_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 
	 * TODO 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
	 * 
	 * @author admin
	 * @date 2017年5月18日
	 * @param length
	 *            随机字符串长度
	 * @return String 随机字符串
	 */
	public static String generateLowerString(int length) {
		return generateMixString(length).toLowerCase();
	}

	/**
	 * 
	 * TODO 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
	 * 
	 * @author HOLI
	 * @date 2017年5月18日
	 * @param length
	 *            随机字符串长度
	 * @return String 随机字符串
	 */
	public static String generateUpperString(int length) {
		return generateMixString(length).toUpperCase();
	}

	/**
	 * 
	 * TODO 生成一个定长的纯0字符串
	 * 
	 * @author HOLI
	 * @date 2017年5月18日
	 * @param length
	 *            字符串长度
	 * @return String 纯0字符串
	 */
	public static String generateZeroString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append('0');
		}
		return sb.toString();
	}

	/**
	 * 
	 * TODO 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @author HOLI
	 * @date 2017年5月18日
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return String 定长的字符串
	 */
	public static String toFixdLengthString(long num, int fixdlenth) {
		StringBuilder sb = new StringBuilder();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		}
		sb.append(strNum);
		return sb.toString();
	}

	/**
	 * 
	 * TODO 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @author HOLI
	 * @date 2017年5月18日
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return String 定长的字符串
	 */
	public static String toFixdLengthString(int num, int fixdlenth) {
		StringBuilder sb = new StringBuilder();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new ServiceException();
		}
		sb.append(strNum);
		return sb.toString();
	}

	/**
	 * 
	 * TODO 随机生成一个随机数根据日期和随机数组
	 * 
	 * @author admin
	 * @date 2017年5月18日
	 * @return String    返回类型
	 */
	public static String genId32AndDate() {
		final int size = 32;
		StringBuilder sb = new StringBuilder();
		sb.append(getDateString());
		sb.append(RandomStringUtils.random(size, "0123456789"));
		return sb.substring(0, size);
	}

}