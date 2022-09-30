package com.example.controllservice.Entiy;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@ToString
@Data
@TableName("bank")
public class bank implements Serializable {
    @TableId
    private String id;
    private Double money;
}
