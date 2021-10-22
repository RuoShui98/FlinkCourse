package com.maikle.study.lesson03;

import com.maikle.study.lesson02.WordCount;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WordCount03 {

    public static void main(String[] args) throws Exception {
        // 1.创建程序入口
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2.数据的输入
        DataStreamSource<String> myDStream = env.socketTextStream("192.168.123.152", 1234);
        // 3.数据的处理
        SingleOutputStreamOperator<WordCount.WordAndOne> result = myDStream.flatMap(new FlatMapFunction<String, WordCount.WordAndOne>() {
            @Override
            public void flatMap(String line, Collector<WordCount.WordAndOne> out) throws Exception {
                String[] fields = line.split(" ");
                for (String word : fields) {
                    out.collect(new WordCount.WordAndOne(word, 1));
                }
            }
        }).keyBy("word")
                .sum("count");


        // 4.数据的输出
        result.print();
        // 5.启动应用程序
        env.execute("wordCount.......");
    }

    public static class WordAndOne {
        private String word;
        private Integer count;

        public WordAndOne(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        public WordAndOne() {
        }

        public String getWord() {
            return word;
        }

        public Integer getCount() {
            return count;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "WordAndOne{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }

}
