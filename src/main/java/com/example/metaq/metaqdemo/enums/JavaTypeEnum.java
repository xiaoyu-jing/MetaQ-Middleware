package com.example.metaq.metaqdemo.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * @author egbert.jxy
 * @date 2021/6/10
 * @describe
 */
@Getter
public enum JavaTypeEnum {

    String_type("String"),

    Integer_type("Integer"),

    Int_type("int"),

    Date_type("Date"),

    Long_type("Long"),

    Double_type("Double"),

    Boolean_type("Boolean");


    private String code;

    JavaTypeEnum(String code) {
        this.code = code;
    }

    public static Map<String, JavaTypeEnum> javaTypeMap = Maps.newHashMap();

    static {
        for (JavaTypeEnum code : JavaTypeEnum.values()) {
            javaTypeMap.put(code.getCode(), code);
        }
    }

    public static JavaTypeEnum getInstance(String code) {
        return javaTypeMap.get(code);
    }
}
