/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.kafka;

import cn.fatri.idis.data.bean.JarvisUser;
import cn.fatri.idis.data.constant.KafkaConstants;
import cn.fatri.idis.data.service.CdhUserSyncService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author: Korol Chen
 * @date: 2020/8/20
 **/
@Slf4j
@Component("applicationConsumerListener")
public class ConsumerListener {

    @Autowired
    private CdhUserSyncService cdhUserSyncService;

    @KafkaListener(topics = {KafkaConstants.KAFKA_TOPIC_USER_LIFECYCLE}, groupId = KafkaConstants.KAFKA_GROUP_ID)
    public void onConnectionMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {
        try {
            log.debug("Received kafka user lifecycle message, consumer record is: [{}]", consumerRecord);
            JSONObject kafkaMessage = JSONObject.parseObject(consumerRecord.value());
            String type = kafkaMessage.getString("type");
            JarvisUser jarvisUser = kafkaMessage.getJSONArray("data").toJavaList(JarvisUser.class).get(0);
            switch (type) {
                case "CREATE":
                    cdhUserSyncService.createUser(jarvisUser);
                    break;
                case "UPDATE":
                    cdhUserSyncService.updateUser(jarvisUser);
                    break;
                case "DELETE":
                    cdhUserSyncService.deleteUser(jarvisUser);
                    break;
                default:
                    break;
            }
            log.debug(kafkaMessage.toJSONString());
        } finally {
            ack.acknowledge();
        }
    }
}
