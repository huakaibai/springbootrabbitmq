package com.zhibinwang.wang;

import com.rabbitmq.client.Channel;
import com.zhibinwang.wang.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author zhibinwang.wang
 * @desc rabbitmq消费着
 **/
@Component
public class Consumer {

    int i = 0 ;
    int j = 0;

    @RabbitListener(queues = RabbitmqConfig.INTEGRAL_DIC_QUEUE)
    public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        byte[] body = message.getBody();
        j++;
        String jsonString = new String(body, "UTF-8");
        System.out.println("接收到消息:"+jsonString);
        System.out.println("j==="+j);
        if (i != 3){
            i++;
            return;
        }
        basicNack(message, channel);


    }

    private void basicNack(Message message, Channel channel) throws IOException {
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);

    }
}
