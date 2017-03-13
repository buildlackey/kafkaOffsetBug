import java.util.Date

import scala.collection.JavaConverters._
import scala.language.implicitConversions

object KafkaMessageWriter {
  var count  = 0

  def writeMessagesToKafka(zkHostPort: String,
                           kafkaTopicName: String,
                           millisecsDelay: Int,
                           numMsgsInBatch: Int,
                           numBatchesOfMsgs: Int) : Unit = {


    val producer = new KafkaProducer(zkHostPort)
    producer.createTopic(kafkaTopicName)
    println(s" topic is :   $kafkaTopicName")
    Thread.sleep(1000)

    (0 to numBatchesOfMsgs).foreach { outerCount =>
      val msgs: List[String] = getBatchOfMessages(outerCount, numMsgsInBatch)
      producer.emitMessages(msgs.asJava, kafkaTopicName)
      Thread.sleep(millisecsDelay)
    }
  }


  def getBatchOfMessages(outerCount: Int, numMsgsInBatch: Int): List[String] = {
    (0 to numMsgsInBatch).map { innerCount =>
      val timeStamp = new Date().getTime
      val filler = "filler" * 100
      val msg = s"""{"count": "$count", "timestamp":"$timeStamp", "filler": "$filler"   }"""
      count = count + 1
      System.out.println("msg:" + msg);
      msg
    }.toList
  }


  def main(args: Array[String]): Unit = {
    writeMessagesToKafka("localhost:2181", "new_topic",  100, 10, 10)
  }
}
