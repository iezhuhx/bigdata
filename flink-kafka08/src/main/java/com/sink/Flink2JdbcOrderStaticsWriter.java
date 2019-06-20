package com.sink;

import com.po.OrderStatics;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
public class Flink2JdbcOrderStaticsWriter extends RichSinkFunction<OrderStatics>{
    private static final long serialVersionUID = -8930276689109741501L;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

    }

    @Override
    public void invoke(OrderStatics value,
                       Context context) throws Exception {
        System.out.println("订单汇总 sink 写入数据库！"+value);
        FlinkH2DbUtils.insertData(value);
    }


    @Override
    public void close() throws Exception {

    }
}
