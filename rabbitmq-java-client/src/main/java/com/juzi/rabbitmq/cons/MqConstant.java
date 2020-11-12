package com.juzi.rabbitmq.cons;

public class MqConstant {

    /*===============================================================================================*/
    // queue
    public static final String DEFAULT_QUEUE = "default_queue";
    public static final String FANOUT_QUEUE = "fanout_queue";
    public static final String DIRECT_SMS_QUEUE = "direct_sms_queue";
    public static final String DIRECT_EMAIL_QUEUE = "direct_email_queue";
    public static final String TOPIC_SMS_QUEUE = "topic_sms_queue";
    public static final String TOPIC_EMAIL_QUEUE = "topic_email_queue";


    /*===============================================================================================*/
    // exchange
    public static final String FANOUT_EXCHANGE = "fanout";
    public static final String DIRECT_EXCHANGE = "direct";
    public static final String TOPIC_EXCHANGE = "topic";

    /*===============================================================================================*/
    // routingKey
    /* 在rabbitMq 中routingKey多个单词之间使用 . 进行分割 */
    public static final String INFORM_SMS = "inform.sms";
    public static final String INFORM_EMAIL = "inform.email";
    public static final String INFORM_SMS_EMAIL = "inform.sms.email";
}
