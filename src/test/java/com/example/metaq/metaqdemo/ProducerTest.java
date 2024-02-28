package com.example.metaq.metaqdemo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.metaq.metaqdemo.domain.Person;
import com.example.metaq.metaqdemo.utils.ClassReflectUtil;
import com.google.common.base.CaseFormat;
import org.junit.Test;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * @author egbert.jxy
 * @date 2021/6/9
 * @describe
 */
public class ProducerTest {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    public void test(){
        String body = "{\"db\":\"xxpt_performance\",\"opt\":\"Delete\","
            + "\"riskId\":\"821cf0ecaf060b50994f03137ea69b4e\",\"table\":[{\"itemMap\":{\"work_name\":\"姓名\","
            + "\"work_nick_name\":\"花名\",\"employ_score_remark\":\"自评评价\",\"manager_score_remark\":\"主管评价\","
            + "\"work_enname\":\"work enname\",\"manager_work_no\":\"实线主管\",\"plan_id\":\"\"},"
            + "\"tableName\":\"perf2_period_document\"},{\"itemMap\":{\"nick\":\"花名\",\"country_code\":\"VA\","
            + "\"name\":\"姓名\",\"nick_en\":\"nick en\",\"name_en\":\"name en\"},"
            + "\"tableName\":\"perf2_frz_emp_info\"},{\"itemMap\":{\"comments\":\"评价\"},"
            + "\"tableName\":\"perf2_goal_scores_new\"},{\"itemMap\":{\"work_name\":\"员工中文名\"},"
            + "\"tableName\":\"emp_build_data\"},{\"itemMap\":{\"pinyin_name\":\"pinyin name\","
            + "\"nick_name\":\"花名\",\"name\":\"姓名\",\"pinyin_nick_name\":\"pinyin nick_name\","
            + "\"email\":\"123@xxx.com\",\"super_work_no\":\"实线主管\"},\"tableName\":\"efr_employee\"},"
            + "{\"itemMap\":{\"country_code\":\"VA\"},\"tableName\":\"perf2_doc_emp_info\"}],"
            + "\"workNo\":\"123\",\"workNoType\":\"EMPLOYEE\"}";

        JSONObject receivedObj = JSONObject.parseObject(body);
        String workNo = receivedObj.getString("workNo");
        System.out.println(workNo);
        JSONArray tableArray = receivedObj.getJSONArray("table");
        for (int i = 0; i < tableArray.size(); i++) {
            JSONObject tableObj = tableArray.getJSONObject(i);
            String tableName = (String)tableObj.get("tableName");
            System.out.println(tableName);
        }

    }

    @Test
    public void test2(){
        String body = "{\"db\":\"xxpt_performance\",\"opt\":\"Delete\","
            + "\"riskId\":\"821cf0ecaf060b50994f03137ea69b4e\",\"table\":[{\"itemMap\":{\"work_name\":\"姓名\","
            + "\"work_nick_name\":\"花名\",\"employ_score_remark\":\"自评评价\",\"manager_score_remark\":\"主管评价\","
            + "\"work_enname\":\"work enname\",\"manager_work_no\":\"实线主管\",\"plan_id\":\"\"},"
            + "\"tableName\":\"perf2_period_document\"},{\"itemMap\":{\"nick\":\"花名\",\"country_code\":\"VA\","
            + "\"name\":\"姓名\",\"nick_en\":\"nick en\",\"name_en\":\"name en\"},"
            + "\"tableName\":\"perf2_frz_emp_info\"},{\"itemMap\":{\"comments\":\"评价\"},"
            + "\"tableName\":\"perf2_goal_scores_new\"},{\"itemMap\":{\"work_name\":\"员工中文名\"},"
            + "\"tableName\":\"emp_build_data\"},{\"itemMap\":{\"pinyin_name\":\"pinyin name\","
            + "\"nick_name\":\"花名\",\"name\":\"姓名\",\"pinyin_nick_name\":\"pinyin nick_name\","
            + "\"email\":\"123@xxx.com\",\"super_work_no\":\"实线主管\"},\"tableName\":\"efr_employee\"},"
            + "{\"itemMap\":{\"country_code\":\"VA\"},\"tableName\":\"perf2_doc_emp_info\"}],"
            + "\"workNo\":\"123\",\"workNoType\":\"EMPLOYEE\"}";

        byte[] bytes = JSONObject.toJSONBytes(body);
        String receivedMsg = new String(bytes, UTF_8);

        //去掉多余的引号和转义字符
        String substring = receivedMsg.substring(1, receivedMsg.length() - 1).replace("\\\"","'");
        //转化为json对象
        //JSONObject jsonObject = new JSONObject(substring);

        JSONObject receivedObj = JSONObject.parseObject(substring);
        String workNo = receivedObj.getString("workNo");
        System.out.println(workNo);
        JSONArray tableArray = receivedObj.getJSONArray("table");
        for (int i = 0; i < tableArray.size(); i++) {
            JSONObject tableObj = tableArray.getJSONObject(i);
            String tableName = (String)tableObj.get("tableName");
            System.out.println(tableName);
        }

    }

    @Test
    public void caseFormatTest(){
        String string_to_format = "work_no";
        String to = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, string_to_format);
        System.out.println("转换驼峰后：" + to);
    }

    @Test
    public void testReflectSet(){
        String body = "{\"db\":\"xxpt_performance\",\"opt\":\"Delete\","
            + "\"riskId\":\"821cf0ecaf060b50994f03137ea69b4e\",\"table\":[{\"itemMap\":{\"name\":\"张三\","
            + "\"name_en\":\"test\",\"age\":\"18\",\"sex\":\"man\",\"care_id\":123456789,\"birthday\":\"2021-01-01\"},"
            + "\"tableName\":\"perf2_period_document\"}],\"workNo\":\"123\",\"workNoType\":\"EMPLOYEE\"}";

        byte[] bytes = JSONObject.toJSONBytes(body);
        String receivedMsg = new String(bytes, UTF_8);

        //去掉多余的引号和转义字符
        String substring = receivedMsg.substring(1, receivedMsg.length() - 1).replace("\\\"","'");

        JSONObject receivedObj = JSONObject.parseObject(substring);
        JSONArray tableArray = receivedObj.getJSONArray("table");
        for (int i = 0; i < tableArray.size(); i++) {
            JSONObject tableObj = tableArray.getJSONObject(i);
            String itemJson = tableObj.getString("itemMap");
            JSONObject itemObj = JSONObject.parseObject(itemJson);

            //更新字段
            Person entity = new Person();
            try {
                Iterator iter = itemObj.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String caseFormatField = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey().toString());

                    //Field field = entity.getClass().getDeclaredField(caseFormatField);
                    //field.setAccessible(true);
                    //field.set(entity,entity,entry.getValue());


                    Field field = Person.class.getDeclaredField(caseFormatField);
                    field.setAccessible(true);

                    String fieldType = field.getType().getSimpleName();
                    Object fieldValue = new Object();
                    if ("String".equals(fieldType)) {
                        fieldValue = entry.getValue().toString();
                    } else if ("Date".equals(fieldType)) {
                        Date fieldDate = parseDate(entry.getValue().toString());
                        fieldValue = fieldDate;
                    } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                        Integer fieldInt = Integer.parseInt(entry.getValue().toString());
                        fieldValue = fieldInt;
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long fieldLong = Long.parseLong(entry.getValue().toString());
                        fieldValue = fieldLong;
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double fieldDouble = Double.parseDouble(entry.getValue().toString());
                        fieldValue = fieldDouble;
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean fieldBoolean = Boolean.parseBoolean(entry.getValue().toString());
                        fieldValue = fieldBoolean;
                    } else {
                        System.out.println("not supper type" + fieldType);
                    }

                    field.set(entity,fieldValue);

                    //Person.class.getField(caseFormatField).set(entity,entry.getValue().toString());
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(entity);

        }

    }



    @Test
    public void testReflectSet2(){
        String body = "{\"db\":\"xxpt_performance\",\"opt\":\"Delete\","
            + "\"riskId\":\"821cf0ecaf060b50994f03137ea69b4e\",\"table\":[{\"itemMap\":{\"name\":\"张三\","
            + "\"name_en\":\"test\",\"age\":\"18\",\"sex\":\"man\",\"care_id\":123456789,\"birthday\":\"2021-01-01\"},"
            + "\"tableName\":\"perf2_period_document\"}],\"workNo\":\"123\",\"workNoType\":\"EMPLOYEE\"}";

        byte[] bytes = JSONObject.toJSONBytes(body);
        String receivedMsg = new String(bytes, UTF_8);

        //去掉多余的引号和转义字符
        String substring = receivedMsg.substring(1, receivedMsg.length() - 1).replace("\\\"","'");

        JSONObject receivedObj = JSONObject.parseObject(substring);
        JSONArray tableArray = receivedObj.getJSONArray("table");
        for (int i = 0; i < tableArray.size(); i++) {
            JSONObject tableObj = tableArray.getJSONObject(i);
            String itemJson = tableObj.getString("itemMap");
            JSONObject itemObj = JSONObject.parseObject(itemJson);

            //更新字段
            Person entity = new Person();
            try {
                Iterator iter = itemObj.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String caseFormatField = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey().toString());
                    //ClassReflectUtil.setFieldValue(caseFormatField,entity,entry);
                    ClassReflectUtil.setFieldValue2(caseFormatField,entity,entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(entity);

        }

    }


    /**
     * 格式化string为Date
     * @param datestr
     * @return date
     */
    private Date parseDate(String datestr) {
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
