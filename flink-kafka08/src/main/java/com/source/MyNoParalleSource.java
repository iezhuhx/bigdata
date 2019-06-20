package com.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 描述：自定义的源
 *
 * @author iechenyb
 * @create --
 */
//使用并行度为1的source
public class MyNoParalleSource implements SourceFunction<String> {//1

    //private long count = 1L;
    private boolean isRunning = true;

    /**
     * 主要的方法
     * 启动一个source
     * 大部分情况下，都需要在这个run方法中实现一个循环，这样就可以循环产生数据了
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        //图书的排行榜
        List<String> books = new ArrayList<>();
        books.add("Pyhton");//10
        books.add("Java");//8
        books.add("Php");//5
        books.add("C++");//3
        books.add("Scala");//0-4
        while(isRunning){
            int i = new Random().nextInt(5);
            ctx.collect(books.get(i));
            //每2秒产生一条数据
            Thread.sleep(2000);
        }
    }
    //取消一个cancel的时候会调用的方法
    @Override
    public void cancel() {
        isRunning = false;
    }
}