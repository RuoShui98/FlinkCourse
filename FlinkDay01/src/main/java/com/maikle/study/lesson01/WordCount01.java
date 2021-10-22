package com.maikle.study.lesson01;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

// 实时统计单词出现的次数

/**
 * @Date 
 */
public class WordCount01 {
    public static void main(String[] args) throws Exception {
        // 1.创建程序入口
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2.数据的输入
        DataStreamSource<String> myDStream = env.socketTextStream("192.168.123.152", 1234);
        // 3.数据的处理
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = myDStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = line.split(" ");
                for (String word : fields) {
                    out.collect(Tuple2.of(word, 1));

                }
            }
        }).keyBy(0).sum(1);
        // 4.数据的输出
        result.print();
        // 5.启动应用程序
        env.execute("wordCount01");
    }
}
