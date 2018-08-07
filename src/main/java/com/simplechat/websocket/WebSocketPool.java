package com.simplechat.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebSocketPool {

    /**
     * map key is userId
     */
    public static Map<String, Set<WebSocketSession>> websockets = new HashMap<>();

}
