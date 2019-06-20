package com.po;

import com.cyb.utils.date.DateUnsafeUtil;
import lombok.Data;

/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
@Data
public class OrderStatics {
    public String name;//商品名称
    public int price;//总价格
    //public String time;
    public OrderStatics(){}
    public OrderStatics(String name,int price){
        this.name = name;
        this.price = price;
       // this.time = DateUnsafeUtil.timeToMilis();
    }
}
