package com.rainbow.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SelectEnum
 * @Description TODO
 * @Author shijunliu
 * @Date 2021/7/13 9:09 上午
 * @Version 1.0
 */
public enum SearchEnum {
	
	IGNORE ("","忽略"),
	EQ("=", " 等于"),
	IN("in", "包含"),
	LIKE("like", "%LIKE%"),
	LIKE_OR("like or", "LIKE OR"),
	LIKE_FRONT("like%", "LIKE_FRONT%"),
	LIKE_AFTER("%like", "%LIKE_AFTER"),
	GREATER(">","greater then"),
	GREATER_EQ(">=","greater then"),
	LESS("<","less then"),
	LESS_EQ("<=","less then"),
	EXISTS_EQ("exists","exists then"),
	EXISTS_IN("exists","exists in"),
	EXISTS_LIKE("exists","exists like"),
	EXISTS_LIKE_FRONT("exists","exists LIKE_FRONT%"),
	EXISTS_LIKE_AFTER("exists","exists %LIKE_AFTER"),
	;
	
	private String code;
	
	private String message;
	
	
	SearchEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static SearchEnum getByCode(String code) {
		if (code == null) {
			return null;
		}
		for (SearchEnum SelectEnum : SearchEnum.values()) {
			if (SelectEnum.getCode().equals(code)) {
				return SelectEnum;
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
		for (SearchEnum type : SearchEnum.values()) {
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
	
	public static Map toMap() {
		Map mp = new HashMap();
		for (SearchEnum type : SearchEnum.values()) {
			mp.put(type.getCode(), type.getMessage());
		}
		return mp;
	}
}
