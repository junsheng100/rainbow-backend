package com.rainbow.base.enums;


import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName StatusEnum
 * @Description TODO
 * @Author shijunliu
 * @Date 2021/7/8 4:39 下午
 * @Version 1.0
 */
public enum ChartEnum {
	BLANK("", ""),
	SPACE(" ", "空格"),
	MIDDLE("-", "-"),
	COMMA(",", "逗号"),
	SEMICOLON(";", "分号"),
	COLON(":", "冒号"),
	DOLLAR("$", "$分割符号"),
	AND("&", "&分割符号"),
	SHARP("#", "#分割符号"),
	VERTICAL("|","|竖线"),
	SLASH("/", "正斜杠"),
	QUOTA1("'", "单引号"),
	QUOTA2("\"", "双引号"),
	EQUAL("=", "相等于"),
	POINT(".","句号"), //semicolon

	TILDE("~","波浪号");
	
	private String code;
	
	private String message;
	
	
	ChartEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static ChartEnum getByCode(String code) {
		if (code == null) {
			return null;
		}
		for (ChartEnum StatusEnum : ChartEnum.values()) {
			if (StatusEnum.getCode().equals(code)) {
				return StatusEnum;
			}
		}
		return null;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public static List<Map<String, Object>> toList() {
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (ChartEnum type : ChartEnum.values()) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("code", type.getCode());
			mp.put("message", type.getMessage());
			mapList.add(mp);
		}
		return mapList;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public static Map<String,String> result ;
	
	public static Map toMap() {
		//if(null == result){
			result = new LinkedHashMap<>();
			for (ChartEnum type : ChartEnum.values()) {
				result.put(type.getCode(), type.getMessage());
			}
		//}
		return result;
	}
	
	public static String getLabel(String code){
		if(StringUtils.isEmpty(code))
			return "";
			Map resMp = toMap();
			if (resMp.containsKey(code)) {
				 String mess = result.get(code)==null?"":result.get(code);
				 return mess;
			}
		return "";
	}
	
	
}
