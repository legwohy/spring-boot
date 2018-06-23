package com.ihome.constant;

/**
 * Created by xuehan on 2017/8/26.
 */
public class RedisConstants {
	public static final String GUESS__USER_SESSION = "guess__user_session_";
	public static final String SEPARATOR = "_";
	public static final String GUESS__USER_VERCODE = "guess__user_vercode_";
	public static final String GUESS__COMPETITIONS = "guess_competitions";
	/** 3天的时间秒 **/
	public static final int THREE_DAY = 259200;
	/** 30分钟时间 **/
	public static final int MINUTE_30 = 1800;

	public static final Long HALF_HOURS_TIME = (long) 30 * 60 * 1000; // millisecond 半小时
	public static final Long HALF_MONTHS_TIME = (long) 15 * 24 * 60 * 60 * 1000; // millisecond 半个月
	public static final Long ONE_MONTHS_TIME = (long) 1 * 24 * 60 * 60 * 1000; // millisecond 一天
	public static final Long TWO_MONTHS_TIME = (long) 2 * 24 * 60 * 60 * 1000; // millisecond 两天
	public static final Long SHREE_MONTHS_TIME = (long) 3 * 24 * 60 * 60 * 1000; // millisecond 3天

}
