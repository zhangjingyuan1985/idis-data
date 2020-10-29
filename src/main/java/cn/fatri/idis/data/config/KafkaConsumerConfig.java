/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

/**
 * @author: Korol Chen
 * @date: 2020/7/21
 **/
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Autowired
    private ConcurrentKafkaListenerContainerFactory containerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory factory() {
        containerFactory.setAutoStartup(false);
        return containerFactory;
    }
}