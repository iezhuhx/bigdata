package com.sink;

import com.po.Order;
import com.po.OrderStatics;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
public class Flink2JdbcOrderWriter extends RichSinkFunction<Order>{
    private static final long serialVersionUID = -8930276689109741501L;
    /*
     * (non-Javadoc)
     *
     * @see org.apache.flink.api.common.functions.AbstractRichFunction#open(org.
     * apache.flink.configuration.Configuration) get database connect
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

    }


    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.flink.streaming.api.functions.sink.SinkFunction#invoke(java.
     * lang.Object,
     * org.apache.flink.streaming.api.functions.sink.SinkFunction.Context) read
     * data from flink DataSet to database
     */
    @Override
    public void invoke(Order value,
                       Context context) throws Exception {
        System.out.println("订单 sink 写入数据库！"+value);
        //FlinkH2DbUtils.insertData(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.flink.api.common.functions.AbstractRichFunction#close()
     * close database connect
     */
    @Override
    public void close() throws Exception {

    }
}
