package com.nitin.test.controller;

import com.nitin.test.entity.Content;
import com.nitin.test.service.ContentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/content")
public class ContentContoller {

    @Autowired
    private ContentService contentService;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return new ResponseEntity<>("Test endpoint reached", HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<?> uploadContent(
            @RequestPart("content") Content content,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        try {
            Content content1 = contentService.uploadContent(content, file);
            return new ResponseEntity<>(content1, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
