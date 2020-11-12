package com.juzi.rabbitmq;

import com.juzi.rabbitmq.cons.MqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProviderTest {
    Connection connection;

    @Before
    public void before() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.25.133");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connection = connectionFactory.newConnection();
    }


    @Test
    public void send() throws IOException {
        Channel channel = connection.createChannel();
        for (int i = 0; i < 200; i++) {
            channel.basicPublish(MqConstant.DIRECT_EXCHANGE, MqConstant.DEFAULT_ROUTINGKEY,null,("msg: 666   " + i).getBytes("utf-8"));
        }


    }
}
