package com.memorynotfound.kafka;

import com.simplechat.messagebroker.Sender;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SimpleChatApplicationTest {

    private static final String EXAMPLE_TOPIC = "example.t";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, EXAMPLE_TOPIC);

    @Autowired
    private Sender sender;

//    @Test
//    public void testReceive() throws Exception {
//        sender.send("Spring Kafka and Spring Boot Configuration Example");
//
//    }

}
