package com.rainbow.user.utils;

import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.exception.ValidException;
import com.rainbow.base.utils.RandomId;
import com.rainbow.base.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class PasswordValidator {

  private static List<String> PASSWORD_LIST;


  private final static String message = "密码只能是字母或数字，且长度不少于8位";

  public boolean validate(String password) {
    if (StringUtils.isEmpty(password))
      throw new ValidException("密码不能为空");

    if (password.length() < 8)
      throw new ValidException(message);

    List<String> codeList = getCodeList();
    List<String> passwordList =  getPasswordList(password);
    for (String p : passwordList) {
      if (!codeList.contains(p))
        throw new ValidException(message);
    }

    return true;
  }

  private List<String> getPasswordList(String password) {
     char[] chars = password.toCharArray();

     List<String> list = new ArrayList<>();
     for(char c : chars){
       String str = String.valueOf(c);
       list.add(str);
     }

     return list;
  }

  public static List<String> getCodeList() {
    if (CollectionUtils.isEmpty(PASSWORD_LIST)) {
      PASSWORD_LIST = new ArrayList<>();
      List<String> code1List = Arrays.asList(RandomId.code.toUpperCase().split(ChartEnum.COMMA.getCode()));
      List<String> code2List = Arrays.asList(RandomId.code.toLowerCase().split(ChartEnum.COMMA.getCode()));
      List<String> numList = Arrays.asList(RandomId.nums.split(ChartEnum.COMMA.getCode()));
      PASSWORD_LIST.addAll(code1List);
      PASSWORD_LIST.addAll(code2List);
      PASSWORD_LIST.addAll(numList);
    }
    return new ArrayList<>(new HashSet<>(PASSWORD_LIST));
  }

} 