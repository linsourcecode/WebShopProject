package com.example.controllservice.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class MqJsonConvert {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Bean
    public MessageConverter messageConverter(){

        return new Jackson2JsonMessageConverter();
    }
  /*  @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(true);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }*/
    @PostConstruct
    public void initRabbitMq(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            // correlationData 唯一id ack是否成功收到
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("exchange receive message");
            }
        });
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                //返回的信息码
                int replyCode = returned.getReplyCode();
                //投递失败的消息的具体信息
                Message message = returned.getMessage();
                //详细内容
                String replyText = returned.getReplyText();
                String routingKey = returned.getRoutingKey();
                String exchange = returned.getExchange();
                log.info(message+" "+replyText);
                log.info("send a object failed");



            }
        });
    }

}
