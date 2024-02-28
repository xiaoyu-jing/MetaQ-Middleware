package com.example.metaq.metaqdemo.service.task2;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSONObject;
import com.taobao.metaq.client.MetaProducer;

/**
 * @author egbert.jxy
 * @date 2021/6/8
 * @describe  海外隐私数据清理-消息发布
 */
public class OverseasPrivacyDataCleanProducer {

    public static void main(String[] args) throws MQClientException, InterruptedException {
        System.out.println("开始发送消息...");
        /**
         * 一个应用创建一个Producer，并指定分组
         */
        MetaProducer producer = new MetaProducer("od-perf-pub-group");

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可
         */
        producer.start();

        /**
         * 发送topic为od-perf-sensitive的消息（海外离职人员隐私数据），默认方式为轮询
         * 注意：send方法是同步调用，只要不抛异常就标识成功。
         */
        try {
            /*String body = "{\"db\":\"xxpt_performance\",\"opt\":\"Delete\","
                + "\"riskId\":\"821cf0ecaf060b50994f03137ea69b4e\",\"table\":[{\"itemMap\":{\"work_name\":\"姓名\"," 
                + "\"work_nick_name\":\"花名\",\"employee_score_remark\":\"自评评价\",\"manager_score_remark\":\"主管评价\","
                + "\"work_enname\":\"work enname\",\"manager_work_no\":\"实线主管\",\"plan_id\":\"\"}," 
                + "\"tableName\":\"perf2_period_document\"},{\"itemMap\":{\"nick\":\"花名\",\"country_code\":\"VA\"," 
                + "\"name\":\"姓名\",\"nick_en\":\"nick en\",\"name_en\":\"name en\"}," 
                + "\"tableName\":\"perf2_frz_emp_info\"},{\"itemMap\":{\"comments\":\"评价\"}," 
                + "\"tableName\":\"perf2_goal_scores_new\"},{\"itemMap\":{\"work_name\":\"员工中文名\"}," 
                + "\"tableName\":\"emp_build_data\"},{\"itemMap\":{\"pinyin_name\":\"pinyin name\"," 
                + "\"nick_name\":\"花名\",\"name\":\"姓名\",\"pinyin_nick_name\":\"pinyin nick_name\"," 
                + "\"email\":\"145591@xxx.com\",\"super_work_no\":\"实线主管\"},\"tableName\":\"efr_employee\"}," 
                + "{\"itemMap\":{\"country_code\":\"VA\"},\"tableName\":\"perf2_doc_emp_info\"}]," 
                + "\"workNo\":\"111482\",\"workNoType\":\"EMPLOYEE\"}";*/


            /*String body = "{\"db\":\"xxpt_performance\",\"opt\":\"Delete\","
                + "\"riskId\":\"821cf0ecaf060b50994f03137ea69b4e\",\"table\":[{\"itemMap\":{\"work_name\":\"姓名\","
                + "\"work_nick_name\":\"花名\",\"employee_score_remark\":\"自评评价\",\"manager_score_remark\":\"主管评价\","
                + "\"work_enname\":\"work enname\",\"manager_work_no\":\"实线主管\",\"plan_id\":\"\"},"
                + "\"tableName\":\"perf2_period_document\"}],\"workNo\":\"123\",\"workNoType\":\"EMPLOYEE\"}";*/

            //小梦的测试
            String body = "{\"db\":\"xxpt_performance\",\"opt\":\"Delete\","
                + "\"riskId\":\"821cf0ecaf060b50994f03137ea69b4e\",\"table\":[{\"itemMap\":{\"work_name\":\"姓名\","
                + "\"work_nick_name\":\"花名\",\"employee_score_remark\":\"自评评价\",\"manager_score_remark\":\"主管评价\","
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
            //JSONObject jsonObject = JSONObject.parseObject(body);
            //jsonObject.getBytes(body);

            Message msg = new Message("od-perf-sensitive",// topic
                "xxpt_performance",// tag
                "od-perf-sensitive001",// key，消息的Key字段是为了唯一标识消息的，方便运维排查问题。如果不设置Key，则无法定位消息丢失原因。
                bytes);// body
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         */
        producer.shutdown();
    }

}
