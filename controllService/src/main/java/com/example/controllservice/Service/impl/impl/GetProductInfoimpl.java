package com.example.controllservice.Service.impl.impl;

import com.example.controllservice.Entiy.CategoryBrandRelationEntity;
import com.example.controllservice.Service.impl.GetProductInfo;
import com.example.controllservice.config.RedissonConfig;
import com.example.controllservice.dao.EntiyMapper;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.scanner.Constant;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

@Service
public class GetProductInfoimpl implements GetProductInfo {
    @Autowired
    private  EntiyMapper entiyMapper;
    @Autowired
    private RedissonConfig redisson;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

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
}
