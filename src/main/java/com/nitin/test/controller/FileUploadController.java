package com.nitin.test.controller;


import com.nitin.test.model.dto.ImageDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${server.location}")
    private String serverLocation;

    private static String UPLOAD_DIR = "uploads/";

    @PostMapping("/images")
    public ResponseEntity<String> uploadImages(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("data") String dataJson) {

        if (files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No files were uploaded");
        }

        try {
            // Parse JSON data
            ObjectMapper objectMapper = new ObjectMapper();
            ImageDataDTO data = objectMapper.readValue(dataJson, ImageDataDTO.class);

            // Save files
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                    String pathh = serverLocation + path;
                    String correctPath = pathh.replace("\\", "/");
                    System.out.println("Corrected URL: " + correctPath);

                    Files.write(path, bytes);
                }
            }

            // Log received data
            System.out.println("Received Data: " + data.getName());


            return ResponseEntity.status(HttpStatus.OK).body("Files and JSON data received successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload files");
        }
    }
}
