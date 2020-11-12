package com.juzi.rabbitmq.consumer;

import com.juzi.rabbitmq.cons.MqConstant;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者
 */
public class Consumer {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.25.133");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            final Channel channel = connection.createChannel();

            /**
             * String queue, boolean autoAck, Consumer callback
             * queue:  队列名称
             * autoAck:  自动回复
             * callback:  接收到消息的处理方法
             */
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                // 处理消息的方法

                /**
                 *
                 * @param consumerTag   消费者标签
                 * @param envelope      信封  用于获取消息的id routingKey 交换机等信息
                 * @param properties    扩展信息
                 * @param body          消息内容
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("consumerTag:  " + consumerTag);
                    System.out.println("routingKey:  " + envelope.getRoutingKey());
                    System.out.println("exchange:  " + envelope.getExchange());
                    System.out.println("deliveryTag:  " + envelope.getDeliveryTag());
                    System.out.println("message:  " + new String(body,"utf-8"));
                    System.out.println("=========================================");

                    // 确认消费
                    /**
                     *  long deliveryTag, boolean multiple
                     *         deliveryTag:  消息id
                     *         multiple:  确认批量接收的消息
                     */
                    channel.basicAck(envelope.getDeliveryTag(),true);
                }
            };
            /**
             * 收到消息手动确认 :
             *      String queue, boolean autoAck, Consumer callback
             *      将 autoAck 设置为false 即可手动确认消费
             */
            channel.basicConsume(MqConstant.DEFAULT_QUEUE,false,defaultConsumer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


