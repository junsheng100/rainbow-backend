package com.rainbow.base.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Account implements Serializable {

    @Schema(title = "ID",type = "String")
    private String id;

    @Schema(title = "名称",type = "String")
    private String name;

    public Date loginTime;

    public Account(){

    }

    public Account(String id, String name, Date loginTime){
        this.id = id;
        this.name = name;
        this.loginTime = loginTime;
    }


}