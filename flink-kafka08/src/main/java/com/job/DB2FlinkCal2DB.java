package com.job;

import com.cyb.pub.config.ServerConfig;
import com.po.Order;
import com.po.OrderStatics;
import com.sink.Flink2JdbcOrderWriter;
import com.sink.Flink2JdbcOrderStaticsWriter;
import com.source.MyRichSourceJdbcReader;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;


/**
 * 描述：
 * flink从kafka中读取字符串并进行统计，两秒统计一次,统计结果写到h2里
 * @author iechenyb
 * @create --
 */
public class DB2FlinkCal2DB {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000);
        DataStream<Order> dataInputStream = env.addSource(new MyRichSourceJdbcReader());//读数据库
        DataStream<OrderStatics> summaryStream = dataInputStream.rebalance()
                .flatMap(new FlatMapFunction<Order, OrderStatics>() {
                    public void flatMap(Order order, Collector<OrderStatics> out) {
                       // System.out.println("处理数据库读取的数据：" + order);
                        out.collect(new OrderStatics(order.getName(),order.getPrice()*order.getNum()));
                    }
                })
                .keyBy("name")
                .timeWindow(Time.seconds(2))
                .reduce(new ReduceFunction<OrderStatics>() {
                    public OrderStatics reduce(OrderStatics a, OrderStatics b) {
                        //System.out.println("执行汇总！！！");
                        return new OrderStatics(a.getName(), a.getPrice() + b.getPrice());
                    }
                });
          writeToDBAfterSummary(summaryStream);//error
          writeToDBDirectly(dataInputStream);//ok
          env.execute("Flink cost DB data to write Database");
    }
    //ok
    static void writeToConsole(DataStream dataStream){
        dataStream.print().setParallelism(1);
    }
    //相同的datastream，不做处理直接写出可以！
    static void writeToDBDirectly(DataStream dataStream){
        dataStream.addSink(new Flink2JdbcOrderWriter());
    }
    //汇总聚合后写出 失败
    static void  writeToDBAfterSummary(DataStream dataStream){
        dataStream.addSink(new Flink2JdbcOrderStaticsWriter());
    }
    //写到kafka 失败
    static void  writeToKafka(DataStream dataStream){
        dataStream.addSink(new FlinkKafkaProducer(ServerConfig.KAFKA224, "json-key", new SimpleStringSchema()));
    }

    //写到redis 尚未尝试
    static void  writeToRedis(DataStream dataStream){

    }
    //写到es 尚未尝试
    static void  writeToEs(DataStream dataStream){

    }
}
