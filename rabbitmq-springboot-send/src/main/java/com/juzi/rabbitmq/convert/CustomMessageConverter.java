package com.juzi.rabbitmq.convert;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.UnsupportedEncodingException;


public class CustomMessageConverter implements MessageConverter {

    /**
     *  用于发送消息时, 将消息进行转换
     * @param o 消息
     * @param messageProperties  消息扩展信息
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        try {
            byte[] content = JSON.toJSONString(o).getBytes("utf-8");
            messageProperties.setContentEncoding("utf-8");
            messageProperties.setContentType("txt/plain");
            System.out.println(messageProperties);
            Message message = new Message(content,messageProperties);
            return message;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return null;
    }
}
