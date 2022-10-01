package com.example.controllservice.Service.impl.impl;

import com.example.controllservice.Entiy.CategoryBrandRelationEntity;
import com.example.controllservice.Entiy.bank;
import com.example.controllservice.Service.impl.GetProductInfo;
import com.example.controllservice.config.RedissonConfig;
import com.example.controllservice.dao.EntiyMapper;
import com.example.controllservice.dao.bankDao;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.scanner.Constant;


import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class GetProductInfoimpl implements GetProductInfo {
    @Autowired
    private  EntiyMapper entiyMapper;
    @Autowired
    private RedissonConfig redisson;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    bankDao bankDao;

    @Override
    public CategoryBrandRelationEntity getCategoryBrandRelationEntity(Long id) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = entiyMapper.selectById(id);
        return categoryBrandRelationEntity;
    }

    @Override
    public boolean existRight(Long id) {
        /**使用布隆过滤器
         *
         * */
        String key = "userlist,"+System.currentTimeMillis()/1000;


        boolean flag;
        RBloomFilter<String> bloomFilter = null;


        bloomFilter = redisson.redissonClient().getBloomFilter(key);
        bloomFilter.tryInit(1000L, 0.03);
        bloomFilter.expire(30, TimeUnit.SECONDS);

        flag = bloomFilter.add(String.valueOf(id));
        System.out.println(flag+" "+key+" "+ String.valueOf(id));

        return flag;
    }
    @RabbitListener(queues = {"lin.java"})
    public void reviceMq(Message message, bank bank, Channel channel) throws IOException {
        byte[] bytes=message.getBody();
        MessageProperties messageProperties=message.getMessageProperties();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{

            //逐个确认收到
            channel.basicAck(deliveryTag,false);
            bankDao.save(bank);

            log.info("receive "+bank+"successfully");
        }catch (Exception exception){
            //long deliveryTag, boolean multiple 批量, boolean requeue 是否回到队列
            channel.basicNack(deliveryTag,false,false);
            log.info("fail："+exception);
        }



    }
    //监听死信队列
    @RabbitListener(queues = {"order.releaseQueue"})
    public void reviceDelayMq(Message message, bank bank, Channel channel) throws IOException {
        byte[] bytes=message.getBody();
        MessageProperties messageProperties=message.getMessageProperties();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{

            //逐个确认收到
            channel.basicAck(deliveryTag,false);
            bankDao.save(bank);

            log.info("listen delay queue  "+bank);
        }catch (Exception exception){
            //long deliveryTag, boolean multiple 批量, boolean requeue 是否回到队列
            channel.basicNack(deliveryTag,false,false);
            log.info("fail："+exception);
        }



    }
    //不区分消息来源
/*    @RabbitHandler
    public void reviceHandler(Message message, bank bank, Channel channel) throws IOException {
        byte[] bytes=message.getBody();
        MessageProperties messageProperties=message.getMessageProperties();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{
            //逐个确认收到
            channel.basicAck(deliveryTag,false);
            log.info(String.valueOf(deliveryTag));
        }catch (Exception exception){
            //long deliveryTag, boolean multiple 批量, boolean requeue 是否回到队列
            channel.basicNack(deliveryTag,false,false);
        }


        log.info("receive "+bank);}*/
}
