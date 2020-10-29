/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

/**
 * @author: Korol Chen
 * @date: 2020/8/20
 **/
@SpringBootApplication
public class DataServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DataServerApplication.class, args);
        context.getBean(KafkaListenerEndpointRegistry.class).getListenerContainers().forEach(Lifecycle::start);
    }
}
