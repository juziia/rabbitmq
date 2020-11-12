package com.juzi.rabbitmq.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MessageService {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(String exchange,String routingKey,String msg) throws UnsupportedEncodingException {
        rabbitTemplate.convertAndSend(exchange,routingKey,new Message(msg.getBytes("utf-8"),new MessageProperties()), new CorrelationData("123456789"));
    }

}
