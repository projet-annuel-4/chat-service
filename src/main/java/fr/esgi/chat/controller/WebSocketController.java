package fr.esgi.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    @MessageMapping("/resume")
    @SendTo("/start/initial")
    public String sendMessage(String message){
        System.out.println(message);
        return message;
    }

}
