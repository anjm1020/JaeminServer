package com.konkuk.JaeminServer.controller;

import com.konkuk.JaeminServer.handler.SocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ServerController {

    private final SocketHandler socketHandler;
    @GetMapping("/")
    public String clientHomePage() {
        return "index";
    }
}
