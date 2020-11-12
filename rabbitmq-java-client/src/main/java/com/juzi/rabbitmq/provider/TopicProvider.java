package com.juzi.rabbitmq.provider;

import com.juzi.rabbitmq.cons.MqConstant;
import com.juzi.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Topics 工作模式生产者
 */
public class TopicProvider {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明队列 String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(MqConstant.TOPIC_EMAIL_QUEUE,true,false,false,null);
        channel.queueDeclare(MqConstant.TOPIC_SMS_QUEUE,true,false,false,null);
        // 声明topic类型的交换机
        channel.exchangeDeclare(MqConstant.TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC,true);
        // 将队列绑定到交换机上 String queue, String exchange, String routingKey
        channel.queueBind(MqConstant.TOPIC_SMS_QUEUE,MqConstant.TOPIC_EXCHANGE,"info.*.sms.*");
        channel.queueBind(MqConstant.TOPIC_EMAIL_QUEUE,MqConstant.TOPIC_EXCHANGE,"info.#.email.#");

        // 发送消息
        String smsMsg = "[topic exchange] send msg to topic_sms_queue ";
        for (int i = 0; i < 3; i++) {
            // 发送消息到 topic_sms_queue中
            channel.basicPublish(
                    MqConstant.TOPIC_EXCHANGE,"info.sms",null,(smsMsg + i).getBytes("utf-8"));
        }

        String emailMsg = "[topic exchange] send msg to topic_sms_queue ";

        for (int i = 0; i < 3; i++) {
            // 发送消息到topic_email_queue中
            channel.basicPublish(
                    MqConstant.TOPIC_EXCHANGE,"info.email",null,(emailMsg + i).getBytes("utf-8")
            );
        }

        for (int i = 3; i < 6; i++) {
            // 发送消息到 topic_sms_queue中 (通过通配符匹配routingKey)
            channel.basicPublish(
                    MqConstant.TOPIC_EXCHANGE, "info.aaa.sms.aa",null,(smsMsg + i).getBytes("utf-8")
            );
        }

        for (int i = 3; i < 6; i++) {
            // 发送消息到topic_email_queue中 (通过通配符匹配routingKey)
            channel.basicPublish(
                    MqConstant.TOPIC_EXCHANGE, "info.aaa.aaa.email.aaa.aaa",null,(emailMsg + i).getBytes("utf-8")
            );
        }

        String smsAndEmailMsg = "[topic exchange] send msg to topic_sms_queue and topic_email_queue ";
        for (int i = 6; i < 9; i++) {
            // 发送消息到topic_email_queue中 (通过通配符匹配routingKey)
            channel.basicPublish(
                    MqConstant.TOPIC_EXCHANGE, "info.aa.sms.email",null,(smsAndEmailMsg + i).getBytes("utf-8")
            );
        }

        channel.close();
        connection.close();
    }
}
