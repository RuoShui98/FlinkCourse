//package com.maikle.oreilly.chapter5
//
//import org.apache.flink.streaming.api.TimeCharacteristic
//import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
//import org.apache.flink.streaming.api.windowing.time.Time
//
///**
// * 5.1: 针对传感器数据流每5秒计算一次平均温度
// */
//object AverageSensorReadings {
//  // 程序入口
//  //def main(args: Array[String]): Unit = {
//
//    // 设置流式执行环境
//    val env = StreamExecutionEnvironment.getExecutionEnvironment
//
//    // 在应用中使用时间时间
//    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
//
//    // 从流式数据源中创建DataStream[SensorReading]对象
//    val sensorData:DataStream[SensorReading] = env
//    // 利用SensorSource SourceFunction获取传感器参数
//      .addSource(new SensorSource)
//    // 分配时间戳和水位线(时间时间所需)
//      .assignTimestampAndWatermarks(new SensorTimeAssigner)
//
//    var avgTemp:DataStream[SensorReading] = sensorData
//    // 使用内联lambda函数把华氏温度转换为摄氏温度
//      .map(r => {
//        val celsius = (r.temperature - 32) * (5.0 / 9.0)
//        SensorReading(r.id, r.timestamp, celsius)
//      })
//    // 按照传感器id组织数据
//      .keyBy(_.id)
//    // 将读数按5s的滚动窗口分组
//      .timeWindow(Time.seconds(5))
//    // 使用用户自定义函数计算平均温度
//      .apply(new TemperatureAverager)
//
//    // 将结果流打印到标准输出
//    avgTemp.print()
//
//    // 开始执行应用
//    env.execute("Compute average sensor temperature")
//  }
//
//}
//
//case class SensorReading(
//    id:String,
//    timestamp:Long,
//    temperature:Double )
