package me.boukadi.kafkaspringapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @KafkaListener(topics = "test-topic-2", groupId = "test-groupe-1")
    public void onMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        System.out.println("=================");
        PageEvent pageEvent = pageEvent(consumerRecord.value());
        System.out.println("Key: " + consumerRecord.key());
        System.out.println("Page: " + pageEvent.getPage() + " Date: "
                + pageEvent.getDate() + " Duration: "
                + pageEvent.getDuration());
        System.out.println("=================");
    }

    private PageEvent pageEvent(String jsonStrPageEvent) throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        PageEvent pageEvent = jsonMapper.readValue(jsonStrPageEvent, PageEvent.class);
        return pageEvent;
    }

}
