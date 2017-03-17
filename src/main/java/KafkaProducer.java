import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class KafkaProducer {
    private String topicName;
    private final String zookeeperHostPort;


    KafkaProducer(String zookeeperHostPort, String topicName) {
        this.topicName = topicName;
        createTopic(topicName);
        this.zookeeperHostPort = zookeeperHostPort;
    }


    KafkaProducer(String zookeeperHostPort) {
        this.zookeeperHostPort = zookeeperHostPort;
    }


    public void emitMessages(List<String> messages, String topicName) {
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        for (String msg : messages) {
            KeyedMessage<String, String> data =
                    new KeyedMessage<String, String>(topicName, msg);
            producer.send(data);
        }
        producer.close();
    }


    public void emitMessages(List<String> messages) {
        emitMessages(messages, topicName);
    }

    public void createTopic(String topicName) {
        ZkClient zkClient = null;
        try {
            String zkHosts = zookeeperHostPort;
            int sessionTimeOutInMs = 15 * 1000; // 15 secs
            int connectionTimeOutInMs = 10 * 1000; // 10 secs

            zkClient = new ZkClient(zkHosts, sessionTimeOutInMs, connectionTimeOutInMs, ZKStringSerializer$.MODULE$);

            int noOfPartitions = 1;
            int noOfReplication = 1;
            Properties topicConfiguration = new Properties();

            ZkUtils zkUtils = ZkUtils.apply(zkClient, false);
            AdminUtils.createTopic(zkUtils , topicName, noOfPartitions, noOfReplication, topicConfiguration, RackAwareMode.Disabled$.MODULE$);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
    }
}
