package com.konkuk.JaeminServer.controller;

import com.konkuk.JaeminServer.handler.SocketHandler;
import com.konkuk.JaeminServer.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ServerController {

    private final SocketHandler socketHandler;
    private final FileService fileService;

    @GetMapping("/")
    public String clientHomePage() {
        return "index";
    }

    @PostMapping("/file")
    public String uploadFile(HttpServletRequest request,
                             @RequestParam("input_file") MultipartFile file,
                             @RequestParam("client_name") String client_name ) throws IOException {

        fileService.uploadFile(client_name, file, request.getRemoteAddr());
        return "index";
    }

    @DeleteMapping("/file/{host}/{fileName}")
    public String deleteFile(HttpServletRequest request,@PathVariable String host, @PathVariable String fileName) throws IOException {
        if (fileService.deleteFile(request.getRemoteAddr(), host, fileName)) {
            return "index";
        }
        return null;
    }

}
