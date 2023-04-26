package com.konkuk.JaeminServer.service;

import com.konkuk.JaeminServer.handler.SocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FileService {

    private final SocketHandler socketHandler;
    private static Map<String, String> pathMap = new ConcurrentHashMap<>();
    private static Map<String, String> originalFilename = new ConcurrentHashMap<>();
    private static final String FILE_DIR = "/Users/jaemin/file";

    public String uploadFile(String host, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {

            String filename = file.getOriginalFilename();
            String fileUUID = UUID.randomUUID().toString() + "_" + filename;

            originalFilename.put(fileUUID, filename);

            String dir = FILE_DIR + File.separator + host;
            if (!pathMap.containsKey(dir)) {
                pathMap.put(dir, host);
            }

            File folder = new File(dir);
            if (!folder.exists()) {
                folder.mkdir();
            }

            String fullPath = dir + File.separator + fileUUID;
            Path absPath = Paths.get(fullPath).toAbsolutePath();
            file.transferTo(absPath.toFile());

            CompletableFuture.runAsync(() -> {
                try {
                    socketHandler.emitFileList(originalFilename);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            return fullPath;
        } else return null;
    }

    public String deleteFile(String host, MultipartFile file) {
        return null;
    }
}
