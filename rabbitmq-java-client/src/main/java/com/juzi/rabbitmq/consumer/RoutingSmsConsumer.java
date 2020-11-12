package com.juzi.rabbitmq.consumer;

import com.juzi.rabbitmq.cons.MqConstant;
import com.juzi.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Routing 工作模式消费者
 *  监听 direct_sms_queue队列
 */
public class RoutingSmsConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(MqConstant.DIRECT_SMS_QUEUE,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("message: " + new String(body,"utf-8"));
            }
        });

    }

}
