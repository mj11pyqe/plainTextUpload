package com.mybug.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        System.out.println("received content-type: " + file.getContentType());
        try {
            if (isValid(file)) { //only allows txt and pdf
                Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
            } else {
                throw new RuntimeException("Content-Type \"" + file.getContentType() + "\" is invalid");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }


    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }


    private boolean isValid(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (contentType == null || !hasSupportedContentType(contentType)) {
            return false;
        }
        return true;
    }

    private boolean hasSupportedContentType(String contentType) {
        return contentType.equals(MediaType.TEXT_PLAIN_VALUE) || contentType.equals(MediaType.APPLICATION_PDF_VALUE);
    }


}
