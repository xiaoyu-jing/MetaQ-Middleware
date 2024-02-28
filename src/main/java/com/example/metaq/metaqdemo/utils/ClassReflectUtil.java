package com.example.metaq.metaqdemo.utils;

import com.example.metaq.metaqdemo.enums.JavaTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author egbert.jxy
 * @date 2021/6/11
 * @describe 反射处理工具类
 *
 * TODO：该工具类按道理来说本来不该属于facade包，但它是专属metaQ反射解析的，这么说，又该属于facade包，所以先放在这里
 */
@Slf4j
public class ClassReflectUtil {

    /**
     * 反射赋值
     * @param clzss
     * @param caseFormatField
     * @param entity
     * @param entry
     */
    public static void setFieldValue(String caseFormatField, Object entity, Map.Entry entry){
        try {
            Field  field = entity.getClass().getDeclaredField(caseFormatField);
            field.setAccessible(true);
            String fieldType = field.getType().getSimpleName();
            Object fieldValue = new Object();

            //此处需要解释一下：JavaTypeEnum是枚举类型，Switch语句中会报错，先用一坨if/else处理
            if (JavaTypeEnum.String_type.getCode().equals(fieldType)) {
                fieldValue = entry.getValue().toString();
            } else if (JavaTypeEnum.Date_type.getCode().equals(fieldType)) {
                Date fieldDate = parseDate(entry.getValue().toString());
                fieldValue = fieldDate;
            } else if (JavaTypeEnum.Integer_type.getCode().equals(fieldType) || JavaTypeEnum.Int_type.getCode().equals(fieldType)) {
                Integer fieldInt = Integer.parseInt(entry.getValue().toString());
                fieldValue = fieldInt;
            } else if (JavaTypeEnum.Long_type.getCode().equalsIgnoreCase(fieldType)) {
                Long fieldLong = Long.parseLong(entry.getValue().toString());
                fieldValue = fieldLong;
            } else if (JavaTypeEnum.Double_type.getCode().equalsIgnoreCase(fieldType)) {
                Double fieldDouble = Double.parseDouble(entry.getValue().toString());
                fieldValue = fieldDouble;
            } else if (JavaTypeEnum.Boolean_type.getCode().equalsIgnoreCase(fieldType)) {
                Boolean fieldBoolean = Boolean.parseBoolean(entry.getValue().toString());
                fieldValue = fieldBoolean;
            } else {
                throw new RuntimeException("not supper type" + fieldType);
            }

             field.set(entity,fieldValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 反射赋值（方式2）
     * @param caseFormatField
     * @param entity
     * @param entry
     */
    public static void setFieldValue2(String caseFormatField, Object entity, Map.Entry entry){
        try {
            Field  field = entity.getClass().getDeclaredField(caseFormatField);
            field.setAccessible(true);
            String fieldType = field.getType().getSimpleName();
            Object fieldValue = new Object();

            switch (JavaTypeEnum.getInstance(fieldType)) {
                case String_type:
                    fieldValue = entry.getValue().toString();
                    break;
                case Date_type:
                    Date fieldDate = parseDate(entry.getValue().toString());
                    fieldValue = fieldDate;
                    break;
                case Integer_type:
                case Int_type:
                    Integer fieldInt = Integer.parseInt(entry.getValue().toString());
                    fieldValue = fieldInt;
                    break;
                case Long_type:
                    Long fieldLong = Long.parseLong(entry.getValue().toString());
                    fieldValue = fieldLong;
                    break;
                case Double_type:
                    Double fieldDouble = Double.parseDouble(entry.getValue().toString());
                    fieldValue = fieldDouble;
                    break;
                case Boolean_type:
                    Boolean fieldBoolean = Boolean.parseBoolean(entry.getValue().toString());
                    fieldValue = fieldBoolean;
                    break;
                /*default:
                    continue;*/
            }

            field.set(entity,fieldValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 格式化string为Date
     * @param datestr
     * @return date
     */
    private static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return new Date();
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                fmtstr = "yyyy-MM-dd HH:mm:ss";
            } else {
                fmtstr = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
            return sdf.parse(datestr);
        } catch (Exception e) {
            return new Date();
        }
    }
}
