package com.motorbike.anqi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	/**
	 * 天包含的毫秒
	 */
	final static long dayMs = 1000*60*60*24;
	/**
	 * 小时包含的毫秒
	 */
	final static long hourMs = 1000*60*60;
	/**
	 * 分钟包含的毫秒
	 */
	final static long minMs = 1000*60;
	/**
	 * 秒包含的毫秒
	 */
	final static long secMs = 1000;
	
	/**
	 * 获取指定毫秒数 包含的天数
	 * @param ms
	 * @return
	 */
	public static int getDay(long ms){
		return (int) (ms / dayMs);
	}
	
	/**
	 * 获取指定毫秒数 包含的(去掉天数以后的)小时
	 * @param ms
	 * @return
	 */
	public static int getHour(long ms){
		long myTotalMs = ms;
		int day = (int) (myTotalMs / dayMs);
		//如果超过了天，减去天
		myTotalMs -= dayMs*day;
		int hour = (int) (myTotalMs / hourMs);
		return hour;
	}
	
	/**
	 * 获取指定毫秒数 包含的(去掉天、小时以后的)分钟
	 * @param ms
	 * @return
	 */
	public static int getMin(long ms){
		long myTotalMs = ms;
		int day = (int) (myTotalMs / dayMs);
		//如果超过了天，减去天
		myTotalMs -= dayMs*day;
		int hour = (int) (myTotalMs / hourMs);
		myTotalMs -= hourMs*hour;
		int min = (int) (myTotalMs / minMs);
		return min;
	}
	
	/**
	 * 获取指定毫秒数 包含的(去掉天、小时、分钟以后的)秒
	 * @param ms
	 * @return
	 */
	public static int getMs(long ms){
		long myTotalMs = ms;
		int day = (int) (myTotalMs / dayMs);
//		//如果超过了天，减去天
//		myTotalMs -= dayMs*day;
//		//超过了小时减去小时
//		int hour = (int) (myTotalMs / hourMs);
//		myTotalMs -= hourMs*hour;
//		//超过了分钟减去分钟
//		int min = (int) (myTotalMs / minMs);
//		myTotalMs -= minMs*min;
		int sec = (int) (myTotalMs / secMs);
		return sec;
	}

	public static int getLimitHour(long time){
		int hour = (int) (time / hourMs);
		return hour;
	}

	public static int getLimitMinute(long time){
		int hour = getLimitHour(time);
		int minute  = (int) ((time - hour * hourMs) / minMs);
		return minute;
	}

	public static int getLimitSecond(long time){
		int hour = getLimitHour(time);
		int minute  = getLimitMinute(time);
		int second = (int) ((time - hour * hourMs - minute * minMs) / secMs);
		return second;
	}

	public static Date parseDate(String str){
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date addTime = null;
		try {
			addTime = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return addTime;
	}
	public static  String getTime(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			String date = sDateFormat.format(new Date());
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String longToStr(Long l){

		String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
				.format(new Date(l));
//		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		@SuppressWarnings("unused")
//				String time=l/1000+"";
//		int i = Integer.parseInt(time);
//		String times = sdr.format(new Date(i));
		return date;
	}
	public static String daunToStrt(long l){
		Date addTime = new Date(l / 1000);
		SimpleDateFormat format =new SimpleDateFormat("MM-dd HH:mm");
		String time = format.format(addTime);
		return time;
	}

	public static Date parseDate(String str, String format){
		SimpleDateFormat dateFormat =new SimpleDateFormat(format);
		Date addTime = null;
		try {
			addTime = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return addTime;
	}

	public static String ParseDateToString(Date date){
		return ParseDateToString(date,"yyyy-MM-DD HH:mm:ss");
	}
	public static String ParseDateToString(String str){
		Date date = parseDate(str);
		return ParseDateToString(date,"yyyy-MM-DD HH:mm:ss");
	}

	public static String ParseDateToString(Date date, String format){
		SimpleDateFormat dateFormat =new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	/**
	 *
	 *@param millis
	 *要转化的毫秒数。
	 *@param isWhole
	  *是否强制全部显示小时/分/秒/毫秒。
	 *@param isFormat
	  *时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
	 *@return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒）。
	 */

	public static String millisToStringMiddle(long millis, boolean isWhole, boolean isFormat) {

		return millisToStringMiddle(millis, isWhole, isFormat, "小时", "分钟", "秒");

	}
	public static String millisToStringMiddle(long millis, boolean isWhole, boolean isFormat,
											  String hUnit, String mUnit, String sUnit) {


		String h = "";

		String m = "";

		String s = "";

		if (isWhole) {

			h = isFormat ? "00" +
					hUnit : "0" +
					hUnit;

			m = isFormat ? "00" +
					mUnit : "0" +
					mUnit;

			s = isFormat ? "00" +
					sUnit : "0" +
					sUnit;

		}


		long temp = millis;


		long hper = 60 * 60 * 1000;

		long mper = 60 * 1000;

		long sper = 1000;


		if (temp / hper > 0) {

			if (isFormat) {

				h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";

			} else {

				h = temp / hper + "";

			}

			h+= hUnit;

		}
		temp = temp % hper;

		if (temp / mper > 0) {
			if (isFormat) {

				m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";

			} else {
				m= temp / mper + "";
			}
			m += mUnit;

		}
		temp = temp % mper;

		if (temp / sper > 0) {
			if (isFormat) {
				s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";

			} else {
				s = temp / sper + "";
			}
			s += sUnit;

		}

		return h + m + s;

	}
	/**
	 * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
	 *
	 * @param timeStamp
	 * @return
	 */
	public static String convertTimeToFormat(long timeStamp) {
		long curTime = System.currentTimeMillis();
		long time = curTime - timeStamp;

		if (time < 60 && time >= 0) {
//			return "刚刚";
//			return new java.text.SimpleDateFormat("hh:mm")
//					.format(new java.util.Date(timeStamp));
			return new SimpleDateFormat("MM-dd HH:mm")
					.format(new Date(timeStamp));
		} else if (time >= 60 && time < 3600) {
//			return time / 60 + "分钟前";
			return new SimpleDateFormat("MM-dd HH:mm")
					.format(new Date(timeStamp));
		} else if (time >= 3600 && time < 3600 * 24) {
//			return time / 3600 + "小时前";
			return new SimpleDateFormat("MM-dd HH:mm")
					.format(new Date(timeStamp));
		} else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
//			return time / 3600 / 24 + "天前";
			return new SimpleDateFormat("MM-dd HH:mm")
					.format(new Date(timeStamp));
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
//			return time / 3600 / 24 / 30 + "个月前";
			return new SimpleDateFormat("MM-dd HH:mm")
					.format(new Date(timeStamp));
		} else if (time >= 3600 * 24 * 30 * 12) {
//			return time / 3600 / 24 / 30 / 12 + "年前";
			return new SimpleDateFormat("MM-dd HH:mm")
					.format(new Date(timeStamp));
		} else {
//			return "刚刚";
//			return new java.text.SimpleDateFormat("hh:mm")
//					.format(new java.util.Date(timeStamp));
			return new SimpleDateFormat("MM-dd HH:mm")
					.format(new Date(timeStamp));
		}
	}

	/**
	 * 将一个时间戳转换成提示性时间字符串，(多少分钟)
	 *
	 * @param timeStamp
	 * @return
	 */
	public static String timeStampToFormat(long timeStamp) {
		long curTime = System.currentTimeMillis() / (long) 1000 ;
		long time = curTime - timeStamp;
		return time/60 + "";
	}


}

