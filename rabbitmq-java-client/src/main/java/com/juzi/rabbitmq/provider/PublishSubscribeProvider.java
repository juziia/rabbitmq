package com.juzi.rabbitmq.provider;

import com.juzi.rabbitmq.cons.MqConstant;
import com.juzi.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * publish/subscribe  工作模式生产者
 */
public class PublishSubscribeProvider {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(MqConstant.FANOUT_QUEUE,true,false,false,null);
        // 声明交换机
        /**
         * String exchange, BuiltinExchangeType type, boolean durable
         *      exchange:  交换机名称
         *      type:  交换机类型 rabbitMq有4种交换机类型 fanout  direct topic headers
         *      durable:  是否持久化
         */
        channel.exchangeDeclare(MqConstant.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        // 将队列绑定到交换机上
        /**
         * String queue, String exchange, String routingKey
         *      queue:  队列名称
         *      exchange:  交换机名称
         *      routingKey: 路由键 交换机通过路由键可以将消息转发给指定的队列
         */
        channel.queueBind(MqConstant.FANOUT_QUEUE,MqConstant.FANOUT_EXCHANGE,"");

        String message = "[fanout exchange]  send msg: ";
        for (int i = 0; i < 6; i++) {

            channel.basicPublish(MqConstant.FANOUT_EXCHANGE,"",null,(message + i).getBytes("utf-8"));
        }

        channel.close();
        connection.close();
    }
}
