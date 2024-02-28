package com.example.metaq.metaqdemo.service.task1;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.taobao.metaq.client.MetaPushConsumer;

import java.util.List;

/**
 * @author jingxiaoyu
 * @date 2018/11/4
 * @describe
 */
public class Consumer {

    /**
     * 当前例子是PushConsumer用法，使用方式给用户感觉是消息从MetaQ服务器推到了应用客户端。 但是实际PushConsumer内部是使用长轮询Pull方式从MetaQ服务器拉消息，然后再回调用户Listener方法
     */
    public static void main(String[] args) throws MQClientException {

        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        MetaPushConsumer consumer = new MetaPushConsumer("XiaoAoTestConsumeGroup");

        /**
         * 一个consumer对象可以订阅多个topic 注意：可以通过指定 Tag 的方式来订阅指定 Topic 下的某一种类型的消息 Tag
         * 表达式说明: 1. * 表示订阅指定 topic 下的所有消息 2. TagA || TagC || TagD 表示订阅指定 topic
         * 下 tags 分别等于 TagA 或 TagC 或 TagD 的消息
         */
        consumer.subscribe("message_distribute_task_performance", "*");
        //consumer.subscribe("message_distribute_task_performance", "TagA");

        //consumer.setConsumeMessageBatchMaxSize(20);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            /**
             * 1、默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
             * 2、如果设置为批量消费方式，要么都成功，要么都失败.
             * 3、此方法由MetaQ客户端多个线程回调，需要应用来处理并发安全问题
             * 4、抛异常与返回ConsumeConcurrentlyStatus.RECONSUME_LATER等价
             * 5、每条消息失败后，会尝试重试，重试5次都失败，则丢弃
             */
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                            ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                System.out.println(Thread.currentThread().getName() + " Receive New " + "Messages: " + list);
                for (MessageExt ext : list) {
                    System.out.println(ext.getBody());
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        /**
         * Consumer对象在使用之前必须要调用start初始化，初始化一次即可
         */
        consumer.start();

        System.out.println("Consumer Started.");

    }
}
