package com.wise.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单实体
 */
@Data
public class Order implements Serializable {

    /**
     * 不支持驼峰
     */
    private String orderName;

    private Float amount;

    private String status;

}
