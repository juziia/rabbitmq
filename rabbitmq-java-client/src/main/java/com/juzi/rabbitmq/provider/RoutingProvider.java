package com.juzi.rabbitmq.provider;


import com.juzi.rabbitmq.cons.MqConstant;
import com.juzi.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Routing 工作模式生产者
 */
public class RoutingProvider {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明direct类型的交换机
        channel.exchangeDeclare(MqConstant.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT,true);
        // 声明队列
        channel.queueDeclare(MqConstant.DIRECT_EMAIL_QUEUE,true,false,false,null);
        channel.queueDeclare(MqConstant.DIRECT_SMS_QUEUE,true,false,false,null);
        // 将队列绑定到交换机上 String queue, String exchange, String routingKey
        channel.queueBind(MqConstant.DIRECT_SMS_QUEUE,MqConstant.DIRECT_EXCHANGE,MqConstant.INFORM_SMS);
        channel.queueBind(MqConstant.DIRECT_EMAIL_QUEUE,MqConstant.DIRECT_EXCHANGE,MqConstant.INFORM_EMAIL);

        // 开始发送消息
        String smsMsg = "[direct exchange] send msg to direct_sms_queue ";
        for (int i = 0; i < 3; i++) {
            // 向 direct_sms_queue中发送3条消息
            // String exchange, String routingKey, BasicProperties props, byte[] body
            channel.basicPublish(
                    MqConstant.DIRECT_EXCHANGE,MqConstant.INFORM_SMS,null,
                    (smsMsg + i).getBytes("utf-8")) ;
        }

        String emailMsg = "[direct exchange] send msg to direct_email_queue ";
        for (int i = 0; i < 3; i++) {
            channel.basicPublish(
                    MqConstant.DIRECT_EXCHANGE,MqConstant.INFORM_EMAIL,null,
                    (emailMsg + i).getBytes("utf-8")
            );
        }

        channel.close();
        connection.close();
    }

}
