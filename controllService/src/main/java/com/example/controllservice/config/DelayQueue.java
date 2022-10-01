package com.example.controllservice.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/***
 * 创建死信队列
 *    order.delay   库存增加
 *    order.release 库存解锁
 * order-create 交换机
 *
 * */
@Configuration
public class DelayQueue {
    @Bean
    Queue orderDelayQueue(){
        Map<String,Object> par=new HashMap<>();
       final String dle = "x-dead-letter-exchange";
       final String dlk = "x-dead-letter-routing-key";
       final String ttl = "x-message-ttl";
       par.put(dle,"order-createExchange");
       par.put(dlk,"release.order");
       par.put(ttl,60000);
       Queue queue=new Queue("order.delayQueue",true,false,false,par);
        return queue;
    }
    @Bean
    Queue orderReleaseQueue(){

        Queue queue=new Queue("order.releaseQueue",true,false,false);
        return queue;
    }
    @Bean
    public Exchange exchange(){
        return new TopicExchange("order-createExchange",true,false);
    }
    @Bean
    public Binding createOrderBinding(){
        return  new Binding("order.delayQueue", Binding.DestinationType.QUEUE,
                "order-createExchange","delay.order",null );
    }
    @Bean
    public Binding releaseOrderBinding(){
        return  new Binding("order.releaseQueue", Binding.DestinationType.QUEUE,
                "order-createExchange","release.order",null );
    }



}
