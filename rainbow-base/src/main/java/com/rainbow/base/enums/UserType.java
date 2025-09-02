package com.rainbow.base.enums;

public enum UserType {
    ADMIN("管理员"),
    COMMON("普通用户");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 