package com.juzi.rabbitmq.config;

import com.juzi.rabbitmq.callback.MessageReturnCallback;
import com.juzi.rabbitmq.callback.PublishConfirmsCallback;
import com.juzi.rabbitmq.cons.MqConstant;
import com.juzi.rabbitmq.convert.CustomMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    /**
     * ===========================================================================================
     */
    // RabbitMq的连接和RabbitTemplate配置
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("192.168.25.133");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        // 开启消息发送方确认
        connectionFactory.setPublisherConfirms(true);

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //消息转换器
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(messageConverter);
//        rabbitTemplate.setMessageConverter(new CustomMessageConverter());

        // 消息确认发送的回调类
        rabbitTemplate.setConfirmCallback(new PublishConfirmsCallback());
        // 开启消息处理失败回调
        rabbitTemplate.setMandatory(true);
        // 消息处理失败回调类
        rabbitTemplate.setReturnCallback(new MessageReturnCallback());

        return rabbitTemplate;
    }


    /**
     * ===========================================================================================
     */
    // 声明消息队列, 交换机
    @Bean
    public Queue defaultQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete
        Map<String, Object> args = new HashMap<>();
        // 死信交换机
        args.put("x-dead-letter-exchange", MqConstant.DEAD_LETTER_EXCHANGE);
        // 配置将消息转发到某个队列的routingKey
        args.put("x-dead-letter-routing-key", MqConstant.DEAD_LETTER);
        // 消息如果超过5000ms 未被消费就认为过期
        args.put("x-message-ttl",5000);

        return new Queue(MqConstant.DEFAULT_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue deadLetterQueue() {

        return new Queue(MqConstant.DEAD_LETTER_QUEUE, true, false, false);
    }


    @Bean
    public Exchange deadLetterExchange() {

        return new DirectExchange(MqConstant.DEAD_LETTER_EXCHANGE,true,false);
    }


    @Bean
    public Binding bindDeadLetterQueueToDeadLetterExchange(){
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange())
                .with(MqConstant.DEAD_LETTER).noargs();
    }

    // 交换机
    @Bean
    public Exchange directExchange() {
        Map<String, Object> args = new HashMap<>();
//        args.put("alternate-exchange", "default_alternate_exchange");

        return ExchangeBuilder.directExchange(MqConstant.DIRECT_EXCHANGE)
                .durable(true).withArguments(args).build();
    }

    //将队列绑定到交换机上
    @Bean
    public Binding bindDefaultQueueToExchange() {

        return BindingBuilder.bind(defaultQueue()).to(directExchange())
                .with(MqConstant.DEFAULT_ROUTINGKEY).noargs();
    }

    /**
     * 备用交换机
     *
     * @return
     */
    @Bean
    public Exchange alternateExchange() {

        return new DirectExchange("default_alternate_exchange", true, false);
    }

    @Bean
    public Binding bindAlternateExchange() {
        return BindingBuilder.bind(defaultQueue()).to(alternateExchange()).with("test").noargs();
    }


}
