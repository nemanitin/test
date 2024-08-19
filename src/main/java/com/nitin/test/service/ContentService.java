package com.nitin.test.service;

import com.nitin.test.entity.Content;
import com.nitin.test.repository.ContentRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Log4j2
@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    @Value("${server.location}")
    private String serverLocation;

    @Value("${upload.directory}")
    private String uploadDirectory;

    private static final String IMAGE_FOLDER = "/uploads/";

    private String getExtension(@NotNull String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public String saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try{
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
            log.info("File uploaded at: {}. File name: {} successfully", uploadPath, fileName);
            return uploadPath+"/"+fileName;
        }
        catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
    }


    public Content uploadContent(Content content, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        // creating image name
        String entityImageName = fileName + "nitin" + getExtension(fileName);
        String entityImagePath = IMAGE_FOLDER + entityImageName;

        Path absolutePath = Paths.get(uploadDirectory, entityImagePath);
        if (Files.exists(absolutePath)) {
            if (Files.isDirectory(absolutePath)) {
                throw new IOException("Path is a directory: " + absolutePath);
            } else {
                Files.delete(absolutePath);
            }
        }

        saveFile(uploadDirectory + IMAGE_FOLDER, entityImageName, file);

        Content content1 = new Content();
        content1.setId(UUID.randomUUID());
        content1.setContentUrl(serverLocation + entityImagePath);
        content1.setImageName(entityImageName);
        content1.setCaption(content.getCaption());
        content1.setTitle(content.getTitle());
        content1.setName(content.getName());

        return contentRepository.save(content1);
    }
}
