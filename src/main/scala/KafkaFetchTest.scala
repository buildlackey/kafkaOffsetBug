
import java.nio.ByteBuffer

import kafka.api.{PartitionOffsetRequestInfo, FetchRequest, FetchRequestBuilder}
import kafka.common.TopicAndPartition
import kafka.javaapi.{OffsetRequest, FetchResponse}
import kafka.javaapi.consumer.SimpleConsumer
import kafka.javaapi.message.ByteBufferMessageSet
import kafka.message.{Message, MessageAndOffset}

import scala.Predef


object KafkaFetchTest {
  val fetchSize = 1000 * 10
  val id: String = "some-clientId"
  val topic: String = "new_topic"
  val partitionId = 0
  val timeOut: Int = 1000
  val consumer = new SimpleConsumer("localhost", 9092, timeOut, fetchSize, id)

  def main(args: Array[String]) {
    println("hi kafka !")
    val timestamp = java.lang.Long.parseLong( args(0) )
    val offset: Long = getOffset(consumer, topic, partitionId, timestamp)
    System.out.println("offset:" + offset);
  }

  def getOffset(consumer: SimpleConsumer, topic: String, partition: Int, startOffsetTime: Long): Long = {
    val topicAndPartition = new TopicAndPartition(topic, partition)
    val requestInfo = new java.util.HashMap[TopicAndPartition, PartitionOffsetRequestInfo]

    requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(startOffsetTime, 1))
    val request: OffsetRequest = new OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion, consumer.clientId)
    val offsets: Array[Long] = consumer.getOffsetsBefore(request).offsets(topic, partition)
    if (offsets.length > 0) {
      offsets(0)
    }
    else {
      throw new RuntimeException("no such offset")
    }
  }
  //1489435068645
  //val offset: Long =  KafkaFetchTest.getOffset(consumer, topic, partitionId, 1489435068300L)
  //System.out.println("offset:" + offset);
}
