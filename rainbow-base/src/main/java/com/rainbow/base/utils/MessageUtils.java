package com.rainbow.base.utils;

/**
 * @ClassName MessageUtils
 * @Description TODO
 * @Author shijunliu
 * @Date 2021/12/16 5:24 下午
 * @Version 1.0
 */

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化工具类
 **/
@Component
public class MessageUtils {

	/**
	 * 根据消息键和参数 获取消息 委托给spring messageSource
	 *
	 * @param code 消息键
	 * @param args 参数
	 * @return 获取国际化翻译值
	 */
	public static String message(String code, Object... args)
	{
		MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
	
//	private static MessageSource messageSource;
//
//	public MessageUtils(MessageSource messageSource) {
//		MessageUtils.messageSource = messageSource;
//	}
//
//	/**
//	 * 获取单个国际化翻译值
//	 */
//	public static String get(String msgKey, Object[] args) {
//		String message = null;
//		try {
//			Locale locale = LocaleContextHolder.getLocale();
//			message = messageSource.getMessage(msgKey, args, locale);
//		} catch (Exception e) {
//			return msgKey;
//		}
//		return message;
//	}
//
//	public static String get(String msgKey) {
//		String message = null;
//		try {
//			Locale locale = LocaleContextHolder.getLocale();
//			message = messageSource.getMessage(msgKey, null, locale);
//		} catch (Exception e) {
//			return msgKey;
//		}
//		return message;
//	}
	
}

