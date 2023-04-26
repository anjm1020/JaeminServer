package com.konkuk.JaeminServer.handler;

import com.sun.tools.jconsole.JConsoleContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Log4j2
public class SocketHandler extends TextWebSocketHandler {

    private static Map<WebSocketSession, String> sockets = new HashMap<>();
    private static final String FILE_DIR = "/Users/jaemin/file";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String remoteAddress = session.getRemoteAddress().toString();
        String username = session.getUri().getQuery().split("=")[1];

        String code = remoteAddress + "#" + username;


        sockets.put(session, code);

        String list = "CLIENT_LIST$";
        for (String value : sockets.values()) {
            System.out.println(value);
            list += value.split("#")[1] + "$";
            System.out.println(list);
        }
        for (WebSocketSession sess : sockets.keySet()) {
            sess.sendMessage(new TextMessage(list));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession sess : sockets.keySet()) {
            sess.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sockets.remove(session);
        String list = "CLIENT_LIST$";
        for (String value : sockets.values()) {
            System.out.println(value);
            list += value.split("#")[1] + "$";
            System.out.println(list);
        }
        for (WebSocketSession sess : sockets.keySet()) {
            sess.sendMessage(new TextMessage(list));
        }
    }

    public void emitFileList(Map<String, String> originalName) throws IOException {
        System.out.println("FILE EMIT");
        for (WebSocketSession sess : sockets.keySet()) {
            String username = sockets.get(sess).split("#")[1];
            String dir = FILE_DIR + File.separator + username;

            String data = "";
            List<String> fileList = Stream.of(new File(dir).listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(file -> originalName.get(file.getName()))
                    .collect(Collectors.toList());
            for (String s : fileList) {
                data += s + "$";
            }

            data = "FILE_LIST$" + data;
            System.out.println(data);
            sess.sendMessage(new TextMessage(data));
        }
    }
}
