
import java.nio.ByteBuffer

import kafka.api.{PartitionOffsetRequestInfo, FetchRequest, FetchRequestBuilder}
import kafka.common.TopicAndPartition
import kafka.javaapi.{OffsetRequest, FetchResponse}
import kafka.javaapi.consumer.SimpleConsumer
import kafka.javaapi.message.ByteBufferMessageSet
import kafka.message.{Message, MessageAndOffset}

import scala.Predef


object KafkaFetcherTest extends App {
  val fetchSize = 1000 * 10
  val id: String = "some-clientId"
  val topic: String = "new_topic"
  val partitionId = 0
  val timeOut: Int = 1000
  val consumer = new SimpleConsumer("localhost", 9092, timeOut, fetchSize, id)

  def rain(args: Array[String]) {

    println("hi kafka !")

    if (args.length  == 1)  {
      val timestamp = java.lang.Long.parseLong( args(1) )
      val offset: Long = getOffset(consumer, topic, partitionId, timestamp)
      System.out.println("offset:" + offset);

    } else {
      testFetch()   // this is a cursory verification that we are using API correctly
    }
  }

  def toByteArray(buffer: ByteBuffer): Array[Byte] = {
    val ret: Array[Byte] = new Array[Byte](buffer.remaining)
    buffer.get(ret, 0, ret.length)
    ret
  }

  // Fetch a batch of message starting at offset 0 and print first one (to verify API usage is correct)
  //
  def testFetch(): Unit = {
    val fetchOffset: Long = 0
    val fetchRequest: FetchRequest =
      (new FetchRequestBuilder).addFetch(topic, partitionId, fetchOffset, fetchSize).clientId(id).maxWait(10000).build
    var fetchResponse: FetchResponse = null
    try {
      fetchResponse = consumer.fetch(fetchRequest)
      val msgs: ByteBufferMessageSet = fetchResponse.messageSet(topic, partitionId)
      if (msgs != null) {
        val msg: Message = msgs.iterator().next().message
        val payload: ByteBuffer = msg.payload
        val bytes: Array[Byte] = toByteArray(payload)
        val message = new Predef.String(bytes, "UTF-8")

        System.out.println("message:" + message);
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
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


  val offset: Long =  KafkaFetchTest.getOffset(consumer, topic, partitionId, 1489435068640L)

  System.out.println("offset:" + offset);
}
