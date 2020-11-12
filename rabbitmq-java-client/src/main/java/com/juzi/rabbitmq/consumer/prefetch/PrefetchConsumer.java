package com.juzi.rabbitmq.consumer.prefetch;

import com.juzi.rabbitmq.cons.MqConstant;
import com.juzi.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *   开启消息预取功能
 */
public class PrefetchConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 每次取10条消息
        channel.basicQos(10);
        final int[] count = {0};
        channel.basicConsume(MqConstant.DEFAULT_QUEUE,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println(new String(body,"utf-8"));
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count[0]++;
                if (count[0] % 10 ==0) {
                    // 批量确认
                    channel.basicAck(envelope.getDeliveryTag(),true);
                }
            }
        });

    }
}
