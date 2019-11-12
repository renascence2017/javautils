package com.javautils.base;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 转换工具类<br>
 * 若待转换值为null或者出现异常，则使用默认值
 *
 */
public class ConvertUtils {
	
	/**
	 * 字符串转换为int
	 *
	 *
	 * @param str	
	 * 					待转换的字符串
	 * @param defaultValue
	 * 					默认值
	 * @return
	 */
	public static int str2Int(String str, int defaultValue) {
		try {
			defaultValue = Integer.parseInt(str);
		} catch (Exception localException) {
		}
		return defaultValue;
	}

	/**
	 * String转换为long
	 *
	 *
	 * @param str
	 * 					待转换字符串
	 * @param defaultValue
	 * 					默认值
	 * @return
	 */
	public static long str2Long(String str, long defaultValue) {
		try {
			defaultValue = Long.parseLong(str);
		} catch (Exception localException) {
		}
		return defaultValue;
	}
	
	/**
	 * 字符串转换为float
	 *
	 *
	 * @param str
	 * 				
	 * @param defaultValue
	 * @return
	 */
	public static float str2Float(String str, float defaultValue) {
		try {
			defaultValue = Float.parseFloat(str);
		} catch (Exception localException) {
		}
		return defaultValue;
	}

	/**
	 * String转换为Double
	 *
	 *
	 * @param str
	 * 					待转换字符串
	 * @param defaultValue
	 * 					默认值
	 * @return
	 */
	public static double str2Double(String str, double defaultValue) {
		try {
			defaultValue = Double.parseDouble(str);
		} catch (Exception localException) {
		}
		return defaultValue;
	}

	/**
	 * 字符串转换日期
	 *
	 *
	 * @param str
	 * 						待转换的字符串
	 * @param defaultValue
	 * 						默认日期
	 * @return
	 */
	public static java.util.Date str2Date(String str,java.util.Date defaultValue) {
		return str2DateByFormat(str, "yyyy-MM-dd HH:mm:ss", defaultValue);
	}

	/**
	 * 字符串转换为指定格式的日期
	 *
	 *
	 * @param str
	 * 					待转换的字符串
	 * @param format
	 * 					日期格式
	 * @param defaultValue
	 * 					默认日期
	 * @return
	 */
	public static java.util.Date str2DateByFormat(String str, String format,java.util.Date defaultValue) {
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		try {
			defaultValue = fmt.parse(str);
		} catch (Exception localException) {
		}
		return defaultValue;
	}

	/**
	 * 日期转换为字符串
	 *
	 *
	 * @param date
	 * 				待转换的日期
	 * @param defaultValue
	 * 				默认字符串
	 * @return
	 */
	public static String date2Str(java.util.Date date, String defaultValue) {
		return date2StrByFormat(date, "yyyy-MM-dd HH:mm:ss", defaultValue);
	}

	/**
	 * 日期转换为指定格式的字符串
	 * 
	 *
	 * @param date
	 * 				待转换的日期
	 * @param format
	 * 				指定格式
	 * @param defaultValue
	 * 				默认值
	 * @return
	 */
	public static String date2StrByFormat(java.util.Date date, String format, String defaultValue) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			defaultValue = sdf.format(date);
		} catch (Exception localException) {
		}
		return defaultValue;
	}

	/**
	 * 如果字符串为空则使用默认字符串
	 *
	 *
	 * @param str
	 * 				字符串
	 * @param defaultValue
	 * 				默认值
	 * @return
	 */
	public static String str2Str(String str, String defaultValue) {
		if ((str != null) && (!(str.isEmpty())))
			defaultValue = str;
		return defaultValue;
	}

	/**
	 * util date 转换为 sqldate
	 *
	 *
	 * @param date
	 * @return
	 */
	public static java.sql.Date date2SqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * sql date 转换为 util date
	 *
	 *
	 * @param date
	 * @return
	 */
	public static java.util.Date sqlDate2Date(java.sql.Date date) {
		return new java.util.Date(date.getTime());
	}

	/**
	 * date 转换为 timestamp
	 *
	 *
	 * @param date
	 * @return
	 */
	public static Timestamp date2SqlTimestamp(java.util.Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * timestamp 转换为date
	 *
	 *
	 * @param date
	 * @return
	 */
	public static java.util.Date qlTimestamp2Date(Timestamp date) {
		return new java.util.Date(date.getTime());
	}

	/**
	 * Bean转换为Map
	 *
	 * @param object
	 * @return String-Object的HashMap
	 *
	 */
	public static Map<String,Object> bean2MapObject(Object object){
		if(object == null){
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(object);

					map.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * Map转换为Java Bean
	 *
	 * @param map
	 *              待转换的Map
	 * @param object
	 *              Java Bean
	 * @return java.lang.Object
	 *
	 */
	public static Object map2Bean(Map map,Object object){
		if(map == null || object == null){
			return null;
		}
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (map.containsKey(key)) {
					Object value = map.get(key);
					// 得到property对应的setter方法
					Method setter = property.getWriteMethod();
					setter.invoke(object, value);
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return object;
	}
}
