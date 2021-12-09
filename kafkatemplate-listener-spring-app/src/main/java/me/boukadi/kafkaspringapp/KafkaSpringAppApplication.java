package me.boukadi.kafkaspringapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaSpringAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringAppApplication.class, args);
    }

}
