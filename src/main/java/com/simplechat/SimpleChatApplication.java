package com.simplechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleChatApplication.class, args);
    }

//    @Autowired
//    private Sender sender;
//
//    @Override
//    public void run(String... strings) throws Exception {
//        sender.send("Spring Kafka and Spring Boot Configuration Example");
//    }
}