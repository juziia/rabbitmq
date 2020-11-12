package com.juzi.rabbitmq.listener;

import com.juzi.rabbitmq.cons.MqConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class MessageListener {


    // 监听队列
    @RabbitListener(queues = MqConstant.DEFAULT_QUEUE,containerFactory = "rabbitListenerContainerFactory")
    public void handleMsg(Message msg, Channel channel) {
        try {
            System.out.println("[消费者1] message: " + new String(msg.getBody(), "utf-8"));

            /**
             *  无论是否消费成功或者消费失败, 都需要响应回复, 否则消息会一直处于unacked状态, 堆积在队列中, 当数据量大的时候
             *  可能会导致RabbitMq性能下降甚至宕机
             *
             * basicNack(long deliveryTag, boolean multiple, boolean requeue): 消费失败
             *      deliveryTag:    消息标识
             *      multiple:       是否批量
             *      requeue:        是否退回队列
             */
//            channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
            channel.basicNack(msg.getMessageProperties().getDeliveryTag(),false,false);
//            Thread.sleep(1500);
//            channel.basicNack(msg.getMessageProperties().getDeliveryTag(),false,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = MqConstant.DEAD_LETTER_QUEUE,containerFactory = "rabbitListenerContainerFactory")
    public void getMsg(Message message, Channel channel) throws IOException {

        System.out.println("[消费者2] message: " + new String(message.getBody(),"utf-8"));

        // 消费成功
        channel.basicAck(message.getMessageProperties().getDeliveryTag()
        ,false);
    }


}
