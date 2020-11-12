package com.juzi.rabbitmq.provider;


import com.juzi.rabbitmq.cons.MqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * 生产者
 */
public class Provider {

    public static void main(String[] args) {
        // 创建连接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 配置连接rabbitmq的信息
        connectionFactory.setHost("192.168.25.133");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        // 创建连接
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            // 使用连接对象创建通道channel
            channel = connection.createChannel();
            // 声明队列
            // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            /**
             * 参数含义:
             *      queue:   队列名称
             *      durable:  队列是否持久化 true 持久化 false 不持久化 (当mq服务关闭则队列会消失)
             *      exclusive:  排他队列, true 则只有当前连接才能操作该队列
             *      autoDelete: 自动删除, 当mq服务关闭时, 会删除该队列  exclusive和autoDelete都为true时, 则是创建了一个临时队列
             *      arguments:  队列的扩展参数
             */
            channel.queueDeclare(MqConstant.DEFAULT_QUEUE,true,false,false,null);

            // 发送消息
            // String exchange, String routingKey, BasicProperties props, byte[] body
            /**
             *  参数含义:
             *      exchange: 交换机名称  设置为"" 则使用rabbitMq默认的交换机
             *      routingKey: 路由键  将消息发送到交换机, 交换机根据routingKey将消息转发到指定的队列上 如果设置为队列名称 则会发送到指定队列上
             *      props: 其他的一些扩展信息
             *      body:  消息内容
             */
            for (int i = 0; i < 100; i++) {

                channel.basicPublish("", MqConstant.DEFAULT_QUEUE,null,"Hello RabbitMq".getBytes("utf-8"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭通道
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 关闭连接
            if (connection != null ) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
