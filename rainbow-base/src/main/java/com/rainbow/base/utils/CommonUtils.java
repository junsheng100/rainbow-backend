package com.rainbow.base.utils;

import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.UseStatus;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jackson.liu
 * @version V1.0
 * @Title:
 * @Package com.yzd.common.utils
 * @Description: CommonUtils.java
 * @date 2019/7/10
 */
public class CommonUtils {


    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static boolean hashStatus(BaseEntity entity, String status){
        if(null == entity || StringUtils.isBlank(entity.getStatus()))
            return false;
        if(StringUtils.isBlank(status))
            return false;
        UseStatus state = UseStatus.getByCode(status);
        if(null == state)
            return false;
        UseStatus entityState = UseStatus.getByCode(entity.getStatus());
        if(null == entityState)
            return false;
        return state.getCode().equals(entityState.getCode());
    }

    public static boolean hashUseStatus(BaseEntity entity, UseStatus status){
        if(null == entity || StringUtils.isBlank(entity.getStatus()))
            return false;
        if(null == status)
            return false;
        UseStatus entityState = UseStatus.getByCode(entity.getStatus());
        if(null == entityState)
            return false;
        return status.getCode().equals(entityState.getCode());
    }

}