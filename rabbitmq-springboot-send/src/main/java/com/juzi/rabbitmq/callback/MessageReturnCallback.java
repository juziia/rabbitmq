package com.juzi.rabbitmq.callback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.UnsupportedEncodingException;

public class MessageReturnCallback implements RabbitTemplate.ReturnCallback {

    /***
     *
     * @param message       消息
     * @param replyCode     状态码
     * @param replyText     错误信息
     * @param exchange      交换机
     * @param routingKey    routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            System.out.println("=======================MessageReturnCallback==========================");
            byte[] body = message.getBody();
            String msg = new String(body,"utf-8");
            System.out.println("message: " + msg);
            System.out.println("replyCode: " + replyCode);
            System.out.println("replyText: " + replyText);
            System.out.println("exchange: " + exchange);
            System.out.println("routingKey: " + routingKey);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
