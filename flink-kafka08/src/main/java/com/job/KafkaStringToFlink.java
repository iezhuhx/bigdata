package com.job;

import com.cyb.pub.config.ServerConfig;
import com.po.WordWithCount;
import com.producer.KafkaMessageFactory;
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
 * flink从kafka中读取字符串并进行统计，两秒统计一次
 * @author iechenyb
 * @create --
 */
public class KafkaStringToFlink {
    public static void main(String[] args) throws Exception {
        KafkaMessageFactory.init();
        KafkaMessageFactory.proString();//kafka造数据
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000);
        /*
     \*  这里主要配置KafkaConsumerConfig需要的属性，如：
     \*  --bootstrap.servers localhost:9092 --topic string-key --group.id test-consumer-group
          */
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        //System.out.println("param topic:"+parameterTool.getRequired("topic"));
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", ServerConfig.KAFKA224);
        //properties.setProperty("zookeeper.connect", "localhost:2181");
        properties.setProperty("group.id", "test");
        FlinkKafkaConsumer kafkaConsumer = new FlinkKafkaConsumer<String>("string-key", new SimpleStringSchema(), properties);
        DataStream<String> dataStream = env.addSource(kafkaConsumer);
        DataStream<WordWithCount> windowCounts = dataStream.rebalance().flatMap(new FlatMapFunction<String, WordWithCount>() {
            public void flatMap(String value, Collector<WordWithCount> out) {
                System.out.println("接收到kafka数据：" + value);
                for (String word : value.split("\\s")) {
                    out.collect(new WordWithCount(word, 1L));
                }
            }
        }).keyBy("word")
                .timeWindow(Time.seconds(2))
                .reduce(new ReduceFunction<WordWithCount>() {
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.word, a.count + b.count);
                    }
                });
        windowCounts.print().setParallelism(1);
        env.execute("com.job.KafkaStringToFlink");
    }

}
