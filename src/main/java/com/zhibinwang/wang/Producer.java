package com.zhibinwang.wang;

import com.zhibinwang.wang.config.RabbitmqConfig;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * author zhibinwang.wang
 *
 * @desc 生产着
 **/
@Component
public class Producer implements RabbitTemplate.ConfirmCallback {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String value) {

        MessageBuilder.withBody(value.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setContentEncoding("utf-8")
                .setMessageId(UUID.randomUUID().toString().replaceAll("-", ""))
                .build();
        this.rabbitTemplate.setMandatory(true);
        //设置confirmcallback实现类
        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(value);
        rabbitTemplate.convertAndSend(RabbitmqConfig.INTEGRAL_EXCHANGE_NAME, RabbitmqConfig.INTEGRAL_ROUTING_KEY,value, correlationData);


    }

    @Override
    public void confirm(CorrelationData CorrelationData, boolean b, String s) {
        String id = CorrelationData.getId();
        if (b){
            System.out.println("消息发送成功："+id);

        }else {
            System.out.println("消息发送失败："+id);

        }

    }
}
