package com.juzi.rabbitmq.callback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class PublishConfirmsCallback implements RabbitTemplate.ConfirmCallback {

    /**
     * @param correlationData 可以获取消息的相关数据
     * @param ack             是否发送成功
     * @param cause           没有发送成功的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("====================PublishConfirmsCallback=============================");
        String id = correlationData.getId();
        Message returnedMessage = correlationData.getReturnedMessage();

        System.out.println("id: " + id);
        System.out.println("returnedMessage: " + returnedMessage);
        System.out.println("ack: " + ack);
        System.out.println("cause: " + cause);
    }
}
