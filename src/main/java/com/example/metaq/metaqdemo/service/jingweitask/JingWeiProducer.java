package com.example.metaq.metaqdemo.service.jingweitask;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.taobao.metaq.client.MetaProducer;

/**
 * @author egbert.jxy
 * @date 2021/07/24
 * @describe
 */
public class JingWeiProducer {

    private static final String topic = "OD-Performance-Decrypt-Data";

    public static void main(String[] args) throws MQClientException, InterruptedException {
        System.out.println("开始发送消息...");
        /**
         * 一个应用创建一个Producer，并指定分组
         */
        MetaProducer producer = new MetaProducer("PID-Performance-Decrypt-Data-Provider");

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可
         */
        producer.start();

        /**
         * 连续发送20个topic为message_distribute_task_1的消息，默认方式为轮询
         * 注意：send方法是同步调用，只要不抛异常就标识成功。
         */
        try {

            Message msg = new Message(topic,// topic
                "*",// tag
                topic + System.currentTimeMillis(),// key，消息的Key字段是为了唯一标识消息的，方便运维排查问题。如果不设置Key，则无法定位消息丢失原因。
                ("Jing Wei Client").getBytes());// body
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
