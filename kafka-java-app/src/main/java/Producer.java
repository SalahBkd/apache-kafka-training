import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Producer {
    private int counter = 0;

    public Producer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.152.3:9092");
        properties.put("client.id", "test-client-1");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        Random random = new Random();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            String key = String.valueOf(counter++);
            String value = String.valueOf(random.nextDouble() * 999999);

            kafkaProducer.send(new ProducerRecord<String, String>("test-topic-2", key, value), (recordMetadata, e) -> {
                System.out.println("Sending message key => " + key + " value => " + value);
                System.out.println("Partition => " + recordMetadata.partition() + " Offset => " + recordMetadata.offset());
            });

        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        new Producer();
    }
}
