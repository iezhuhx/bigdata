package com.job;

import com.cyb.pub.config.ServerConfig;
import com.source.MyNoParalleSource;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

/**
 * 描述：读取list 成功写入kafka
 *D:\bigdata\kafka_2.11-2.2.1\kafka_2.11-2.2.1\bin\windows>kafka-console-consumer.
 bat  --bootstrap-server 192.168.108.224:9092 --topic  json-key
 * @author iechenyb
 * @create --
 */
public class MySource2Kafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> text = env.addSource(new MyNoParalleSource()).setParallelism(1);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", ServerConfig.KAFKA224);
        //new FlinkKafkaProducer("topn",new KeyedSerializationSchemaWrapper(new SimpleStringSchema()),properties,FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
        FlinkKafkaProducer<String> producer =
                new FlinkKafkaProducer("json-key",
                new SimpleStringSchema(),
                properties);
        /*
        //event-timestamp事件的发生时间
        producer.setWriteTimestampToKafka(true);
        */
        text.addSink(producer);
        env.execute(" any name !");
    }
}
