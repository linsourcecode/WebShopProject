package com.example.controllservice.Service.impl;

import com.example.controllservice.Entiy.CategoryBrandRelationEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.websocket.BackgroundProcess;

public interface GetProductInfo  {
    /***/
    public CategoryBrandRelationEntity getCategoryBrandRelationEntity(Long id);
    /****
     * 传递用户的id,判断用户有权限拉取数据库信息
     * */
    boolean existRight(Long id);
}
