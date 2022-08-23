package com.example.controllservice.controller;

import com.example.controllservice.Entiy.CategoryBrandRelationEntity;
import com.example.controllservice.Service.impl.GetProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.geom.GeneralPath;

/**
 *
 * 提供商品信息调用接口
 * */
@RestController
@RequestMapping("getinfo")
public class ProductInfoController {
    @Autowired
    GetProductInfo getProductInfo;
    @RequestMapping("/getbrand")
    public String categoryBrandRelationEntity(Long userid,Long id) {
        if (getProductInfo.existRight(userid)) {
            CategoryBrandRelationEntity categoryBrandRelationEntity = getProductInfo.getCategoryBrandRelationEntity(id);
            return categoryBrandRelationEntity.toString();

        } else {

            return "刷新过快,稍后重试";


        }

    }
}
