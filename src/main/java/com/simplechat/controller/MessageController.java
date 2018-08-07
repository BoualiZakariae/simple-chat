package com.simplechat.controller;

import com.simplechat.messagebroker.Sender;
import com.simplechat.model.Message;
import com.simplechat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;

import java.util.List;


@RestController
public class MessageController {

    @Autowired
    private Sender sender;

//    @RequestMapping(value = "/send", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<String> serviceRegister(@RequestBody String request) {
//
//        JSONObject jsonObjectRequest = new JSONObject(request);
//
//        sender.send("salam");
//
//        return new ResponseEntity<String>(new JSONObject().put("status", "ok").toString(), HttpStatus.OK);
//    }

    @Autowired
    MessageRepository messageRepository;

    @RequestMapping(value = "/showChat", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> showChat(@RequestBody String request) {

        JSONObject jsonObjectRequest = new JSONObject(request);

        List<Message> m = messageRepository.findBySenderAndReciever("1", "2");

        return new ResponseEntity<String>(new JSONObject().put("status", m.toString()).toString(), HttpStatus.OK);
    }

}
