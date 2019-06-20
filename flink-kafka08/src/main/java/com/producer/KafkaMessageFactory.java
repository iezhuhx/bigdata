package com.producer;

import com.alibaba.fastjson.JSONObject;
import com.cyb.pub.config.ServerConfig;
import com.cyb.utils.date.DateUnsafeUtil;
import com.cyb.utils.random.RandomUtils;
import com.po.Goods;
import org.apache.kafka.clients.producer.*;
import  com.po.Order;

import java.util.*;
import java.util.concurrent.Future;

/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
public class KafkaMessageFactory {
    static Properties props = null;
    public static void init(){
        props = new Properties();
        Object put = props.put("bootstrap.servers", ServerConfig.KAFKA224);
        props.put("acks", "all");//同步所有节点
        props.put("retries", 0);//失败，则不尝试
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 1024*1024);//33554432
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }
    public static void main(String[] args) throws Exception {
        init();
        proJson();
        List<Goods> goods = goods();
        /*for(int i=0;i<100;i++) {
            proJsonOrder(goods);
            Thread.sleep(10*1000);//隔10s生成一批订单
        }*/
    }
    public static List<Goods> goods(){
        Goods goods;
        List<String> goodsNames = new ArrayList<>();
        goodsNames.add("iphone8");
        goodsNames.add("thinking in java");
        goodsNames.add("mate10");
        goodsNames.add("meiz20");
        goodsNames.add("oppo 30x");
        int goodsNums=5;
        List<Goods> goodsData = new ArrayList<>(goodsNums);
        for(int i=0;i<goodsNums;i++){
            goods = new Goods(goodsNames.get(RandomUtils.getNum(0,goodsNames.size()-1)),RandomUtils.getNum(20,50));
            goodsData.add(goods);//随机生成商品
        }
        return goodsData;
    }
    public static void proJsonOrder() throws InterruptedException {
        proJsonOrder(null);
    }
    static int orderNums=50;//模拟订单数
    public static List<Order> getOrders() throws InterruptedException {
        return getOrders(null);
    }
    public static List<Order> getOrders(List<Goods> goodsData) throws InterruptedException {
        if(goodsData == null) goodsData =goods();
        Order order;
        List<Order> data = new ArrayList<>(orderNums);
        for (int i = 0; i < orderNums; i++) {
            order = new Order();
            Goods good = goodsData.get(RandomUtils.getNum(0,goodsData.size()-1));
            order.setName(good.getName());//商品名称
            order.setOrderId(RandomUtils.getNum(1,1000));//订单号
            order.setNum(RandomUtils.getNum(1,10));//数量
            order.setPrice(good.getPrice());//单价
            //order.setTime("");
            data.add(order);
           /* long time = 1L*RandomUtils.getNum(1,500);
            System.out.println(" sleep:"+time);
            Thread.sleep(time);*/
        }
        return data;
    }


    public static void proJsonOrder(List<Goods> goodsData) throws InterruptedException {
        List<Order> data = getOrders(goodsData);
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        int totalMessageCount = 50;//模拟50单
        for (int i = 0; i < totalMessageCount; i++) {
            Order order_ = data.get(RandomUtils.getNum(0,orderNums-1));
            String value = JSONObject.toJSONString(order_);
            Future<RecordMetadata> send = producer.send(new ProducerRecord<>("json-key", value), new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        System.out.println("Failed to send message with exception " + exception);
                    }
                }
            });
            long time = 1L*RandomUtils.getNum(1,10);
            System.out.println(value+" sleep:"+time);
            Thread.sleep(time);
        }
        Map<String,Integer> orderSum = new HashMap<>();//订单汇总
        for (int i = 0; i < orderNums; i++) {
            Order order_tmp = data.get(i);
         if(orderSum.containsKey(order_tmp.getName())){
             int total = orderSum.get(order_tmp.getName())+order_tmp.getNum()*order_tmp.getPrice();
             orderSum.put(order_tmp.getName(),total);
         }else{
             orderSum.put(order_tmp.getName(),order_tmp.getNum()*order_tmp.getPrice());
         }
        }
        System.out.println("订单汇总（校对）信息："+orderSum);

        producer.close();
    }
    public static void proJson() throws InterruptedException {
        Order order;
        int orderNums=50;
        List<Order> data = new ArrayList<>(orderNums);
        for (int i = 0; i < orderNums; i++) {
            order = new Order();
            order.setName(RandomUtils.getPassWord(5));
            order.setOrderId(RandomUtils.getNum(1,1000));
            order.setNum(RandomUtils.getNum(1,10));
            order.setPrice(RandomUtils.getNum(20,50));
            data.add(order);
        }

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        int totalMessageCount = 10000;
        for (int i = 0; i < totalMessageCount; i++) {
            Order order_ = data.get(RandomUtils.getNum(0,orderNums-1));
            String value = JSONObject.toJSONString(order_);
            Future<RecordMetadata> send = producer.send(new ProducerRecord<>("json-key", value), new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        System.out.println("Failed to send message with exception " + exception);
                    }
                }
            });
            long time = 10L*RandomUtils.getNum(1,10);
            System.out.println(value+" sleep:"+time);
            Thread.sleep(time);
        }
        producer.close();
    }

    public static void proString() throws InterruptedException {
        int wordNums=50;
        List<String> data = new ArrayList<>(wordNums);
        for (int i = 0; i < wordNums; i++) {
            data.add(RandomUtils.getPassWord(5));
        }

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        int totalMessageCount = 10000;
        for (int i = 0; i < totalMessageCount; i++) {
            String value = data.get(RandomUtils.getNum(0,wordNums-1));
            Future<RecordMetadata> send = producer.send(new ProducerRecord<>("string-key", value), new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        System.out.println("Failed to send message with exception " + exception);
                    }
                }
            });
            long time = 10L*RandomUtils.getNum(1,10);
            System.out.println(value+" sleep:"+time);
            Thread.sleep(time);
        }
        producer.close();
    }

}
