package com.source;

import com.cyb.utils.date.DateUnsafeUtil;
import com.po.Order;
import com.po.OrderStatics;
import com.producer.KafkaMessageFactory;
import com.sink.FlinkH2DbUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
public class MyRichSourceJdbcReader extends
        RichSourceFunction<Order> {
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.flink.streaming.api.functions.source.SourceFunction#run(org.
     * apache.flink.streaming.api.functions.source.SourceFunction.SourceContext)
     * to use excuted sql and return result
     */
    @Override
    public void run(SourceContext<Order> collect)
            throws Exception {

        List<Order> data = KafkaMessageFactory.getOrders();
        System.out.println(DateUnsafeUtil.timeToMilis()+":模拟读取数据库！\n data is:"+data);
        for(Order order:data){
            collect.collect(order);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.flink.streaming.api.functions.source.SourceFunction#cancel()
     * colse database connect
     */
    @Override
    public void cancel() {


    }
}
