package com.start.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间工具类
 * @author Start
 */
public class TimeUtils {
	
	public final static String yyyyMMdd_C="yyyy年MM月dd日";
	
	public final static String yyyyMM_F="yyyy-MM";
	public final static String yyyyMMdd_F="yyyy-MM-dd";
	public final static String yyyyMMddHHmmss_F="yyyy-MM-dd HH:mm:ss";
	public final static String yyyyMMddHHmmsssss_F="yyyy-MM-dd HH:mm:ss,SSS";

	public final static String yyyy="yyyy";
	public final static String yyyyMM="yyyyMM";
	public final static String yyyyMMdd="yyyyMMdd";
	public final static String yyyyMMddHH="yyyyMMddHH";
	public final static String yyyyMMddHHmmss="yyyyMMddHHmmss";
	public final static String yyyyMMddHHmmsssss="yyyyMMddHHmmssSSS";
	/**
	 * 取得系统时间
	 */
    public static String getSysTime(String pattern) {
        return formatSysTime(new SimpleDateFormat(pattern, Locale.ENGLISH));
    }

    /**
     * 格式化系统时间
     */
    private static String formatSysTime(SimpleDateFormat format) {
        String str = format.format(Calendar.getInstance().getTime());
        return str;
    }

    public static String format(Date date, String pattern) {
    	SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String str = format.format(date);
		return str;
    }

    public static Date getDate(int days) {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime(); 
    }

    public static Date getHour(int hours) {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, hours);
		return cal.getTime(); 
    }

    public static boolean validTime(String str, String pattern) {
    	DateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
		Date date = null;
		try {
			date = (Date) formatter.parse(str);
		} catch (ParseException e) {
//			LogUtils.logError(StackTraceInfo.getTraceInfo() + e.getMessage());
			return false;
		}
		return str.equals(formatter.format(date));
	}

    public static Date format(String str, String pattern) {
    	DateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
		Date date = null;
		try {
			date = (Date) formatter.parse(str);
		} catch (ParseException e) {
//			LogUtils.logError(StackTraceInfo.getTraceInfo() + e.getMessage());
			return null;
		}
		return date;
	}

    public static String getSysYear() {
        return getSysTime(yyyy);
    }

    public static String getSysTime() {
        return getSysTime(yyyyMMddHHmmss_F);
    }

    public static String getSysTimeS() {
        return getSysTime(yyyyMMddHHmmsssss_F);
    }

    public static String getSysTimeLong() {
        return getSysTime(yyyyMMddHHmmss);
    }

    public static String getSysTimeSLong() {
        return getSysTime(yyyyMMddHHmmsssss);
    }

    public static String getSysdate() {
        return getSysTime(yyyyMMdd_F);
    }

    public static String getSysyearmonthInt() {
        return getSysTime(yyyyMM);
    }

    public static String getSysdateInt() {
        return getSysTime(yyyyMMdd);
    }

    public static String getSysdateTimeStart() {
        return getSysdate() + " 00:00:00";
    }

    public static String getSysdateTimeEnd() {
        return getSysdate() + " 23:59:59";
    }

    public static String getSysDateLocal() {
        return getSysTime(yyyyMMdd_C);
    }

    public static String getTimeFormat(String str) {
        return format(format(str, yyyyMMddHHmmss), yyyyMMddHHmmss_F);
    }

    public static String getDateFormat(String str) {
        return format(format(str, yyyyMMddHHmmss_F), yyyyMMdd_F);
    }

    public static String getDateFormatLocal(String str) {
        return format(format(str, yyyyMMddHHmmss_F), yyyyMMdd_C);
    }
    /**
     * 获取前一天日期格式
     */
    public static String getTheDayBefore() {
        return format(getDate(-1), yyyyMMdd);
    }
    /**
     * 获取前一天日期格式
     */
    public static String getTheDayBefore_F() {
        return format(getDate(-1), yyyyMMdd_F);
    }

    public static String getDateFormat(int days) {
        return format(getDate(days), yyyyMMdd_F);
    }

    public static String getDateFormatLocal(int days) {
        return format(getDate(days), yyyyMMdd_C);
    }

    public static String getTimeFormatHour(int hours) {
        return format(getHour(hours), yyyyMMddHHmmss_F);
    }

	public static Date getHourTime(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
//		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date time = calendar.getTime();
		return time;
	}

	public static Date getHourTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
//		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date time = calendar.getTime();
		return time;
	}

	public static Date getTomorrowHourTime(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date time = calendar.getTime();
		return time;
	}

	public static Date getTomorrowHourTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date time = calendar.getTime();
		return time;
	}

	public static Date getThismonthTime(int day, int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date time = calendar.getTime();
		return time;
	}

	public static Date getNextmonthTime(int day, int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date time = calendar.getTime();
		return time;
	}

	public static Date getMinute(int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, minute);
		Date time = calendar.getTime();
		return time;
	}

	public static Date getSecond(int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, second);
		Date time = calendar.getTime();
		return time;
	}

	public static long subtract(String start) {
	    return format(getSysTimeS(), yyyyMMddHHmmsssss_F).getTime() - format(start, yyyyMMddHHmmsssss_F).getTime();
	}

	public static long subtract(String end, String start) {
	    return format(end, yyyyMMddHHmmsssss_F).getTime() - format(start, yyyyMMddHHmmsssss_F).getTime();
	}

	public static String getGMTTime(String TimeZoneFormat) {
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		format.setTimeZone(TimeZone.getTimeZone(TimeZoneFormat));
		String time = format.format(Calendar.getInstance().getTime());
		return time;
	}

	public static String getGMTTime() {
		return getGMTTime("GMT");
	}

	public static String getGMT8Time() {
		return getGMTTime("GMT+08:00");
	}
	
	public static void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String convertTime(int time){
		time/=1000;
		int minute=time/60;
		int hour=minute/60;
		int second=time%60;
		minute%=60;
		if(hour>0){
			return String.format("%02d:%02d:%02d",hour,minute,second);
		}
        return String.format("%02d:%02d",minute,second);
	}
	
	public static String customerTimeConvert(String str){
		SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss_F);
		try {
			Date now = sdf.parse(sdf.format(new Date()));
			Date date = sdf.parse(str);
			long l = now.getTime() - date.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
//			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//			System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒-原："+str);
			if(day>0){
				if(day<=7){
					return day+"天前";
				}else{
					SimpleDateFormat sdf1=new SimpleDateFormat(yyyyMMdd_C);
					return sdf1.format(date);
				}
			}
			if(hour>0){
				return hour+"小时前";
			}
			if(min>0){
				return min+"分钟前";
			}
			
		} catch (ParseException e) {
			return str;
		}
		return "刚刚";
	}
	/**
	 * 秒转化为时间
	 * sample:61秒转成1分1秒
	 */
	public static String secondConvertTime(int second){
		int day=second/(60*60*24);
		int h=(second%(60*60*24))/(60*60);
		int m=((second%(60*60*24))%(60*60))/60;
		int s=(((second%(60*60*24))%(60*60))%60)%60;
		StringBuilder sb=new StringBuilder();
		if(day>0){
			sb.append(day+"天");
			sb.append(h+"小时");
			sb.append(m+"分");
			sb.append(s+"秒");
		}else if(h>0){
			sb.append(h+"小时");
			sb.append(m+"分");
			sb.append(s+"秒");
		}else if(m>0){
			sb.append(m+"分");
			sb.append(s+"秒");
		}else{
			sb.append(s+"秒");
		}
		return sb.toString();
	}
	/**
	 * 获取当前月的天数
	 * @return
	 */
	public static int getMonthNum(int year,int month){
		int days = 0; // 存储当月的天数
		boolean isRn;
		/* 判断是否是闰年 */
		if (year % 4 == 0 && !(year % 100 == 0) || year % 400 == 0) { // 判断是否为闰年
			isRn = true; // 闰年
		} else {
			isRn = false;// 平年
		}
		/* 计算当月的天数 */
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days = 31;
			break;
		case 2:
			if (isRn) {
				days = 29;
			} else {
				days = 28;
			}
			break;
		default:
			days = 30;
			break;
		}
		return days;
	}
}