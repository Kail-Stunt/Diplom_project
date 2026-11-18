package ru.diplom.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private final Path fileStorageLocation;

    public ImageService(@Value("${app.storage.path:./uploads}") String storagePath) {
        this.fileStorageLocation = Paths.get(storagePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String storeImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetLocation = fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation);
        return "/images/" + fileName;
    }

    public byte[] getImage(String imagePath) throws IOException {
        String fileName = imagePath.substring("/images/".length());
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        return Files.readAllBytes(filePath);
    }
}
