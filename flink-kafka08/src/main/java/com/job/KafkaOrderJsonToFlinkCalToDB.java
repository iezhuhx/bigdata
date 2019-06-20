package com.job;

import com.alibaba.fastjson.JSONObject;
import com.cyb.pub.config.ServerConfig;
import com.po.Order;
import com.po.OrderStatics;
import com.producer.KafkaMessageFactory;
import com.sink.Flink2JdbcOrderStaticsWriter;
import com.sink.FlinkH2DbUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Properties;


/**
 * 描述：
 * flink从kafka中读取字符串并进行统计，两秒统计一次,统计结果写到h2里
 * @author iechenyb
 * @create --
 */
public class KafkaOrderJsonToFlinkCalToDB {
    public static void main(String[] args) throws Exception {
        KafkaMessageFactory.init();
        KafkaMessageFactory.proJsonOrder();//造数据
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000);

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", ServerConfig.KAFKA224);
        properties.setProperty("group.id", "test");
        DataStream<String> dataInputStream = env.addSource(new FlinkKafkaConsumer<String>("json-key", new SimpleStringSchema(), properties));
        DataStream<OrderStatics> windowCounts = dataInputStream.rebalance().flatMap(new FlatMapFunction<String, OrderStatics>() {
            public void flatMap(String value, Collector<OrderStatics> out) {
                Order order =  JSONObject.parseObject(value, Order.class);
                out.collect(new OrderStatics(order.getName(),order.getPrice()*order.getNum()));
            }
        }).keyBy("name")
                .timeWindow(Time.seconds(2))
                .reduce(new ReduceFunction<OrderStatics>() {
                    public OrderStatics reduce(OrderStatics a, OrderStatics b) {
                        return new OrderStatics(a.getName(), a.getPrice() + b.getPrice());
                    }
                });
        windowCounts.print().setParallelism(1);
        windowCounts.addSink(new Flink2JdbcOrderStaticsWriter());//ok
        env.execute("com.job.KafkaStringToFlink");
    }

}
