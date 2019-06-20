package com.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    private String name;//商品名称
    private int price;//单价价格
}
