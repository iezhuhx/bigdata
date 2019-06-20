package com.po;

import lombok.Data;

/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
@Data
public class Order {
    private int orderId;//订单号
    private String name;//商品名称
    private int price;//价格
    private int num;//数量
    //private String time;
}
