package me.boukadi.springcloudstreamkafkaapp.service;

import me.boukadi.springcloudstreamkafkaapp.entity.PageEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class PageEventService {
    @Bean
    public Consumer<PageEvent> pageEventConsumer() {
        // each time we have an event this consumer gets executed
        return (input) -> {
            System.out.println("=======================");
            System.out.println(input.toString());
            System.out.println("=======================");
        };
    }

    @Bean
    public Supplier<PageEvent> pageEventSupplier() {
        // each second springcloudstream will run this supplier function, the timer is customizable in apps.properties
        return () -> new PageEvent(Math.random()>0.5 ? "P1" : "P2", Math.random()>0.5 ? "U1" : "U2", new Date(),new Random().nextInt(5000));
    }

    @Bean
    public Function<PageEvent, PageEvent> pageEventFunction() {
        return (input) -> {
            input.setName("modified name");
            input.setUser("modified user");
            return input;
        };
    }

    @Bean
    public Function<KStream<String, PageEvent>,KStream<String, Long>> kStreamFunction() {
        return (input) -> input
                .filter((key, value) -> value.getDuration() > 100)
                .map((key, value) -> new KeyValue<>(value.getName(), 0L))
                .groupBy((key, value) -> key, Grouped.with(Serdes.String(), Serdes.Long()))
                .windowedBy(TimeWindows.of(Duration.ofMillis(5000L))) // Only Data in stream for the last 5 seconds, it returns KTable
                .count(Materialized.as("page-count"))
                .toStream()
                // we convert it to KStream as Key-Value pair
                .map((k,v)->new KeyValue<>("=>"+k.window().startTime()+k.window().endTime()+":"+k.key(),v));
    }
}
