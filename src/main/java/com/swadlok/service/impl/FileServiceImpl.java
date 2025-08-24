package com.swadlok.service.impl;

import com.swadlok.dto.FileUploadResponse;
import com.swadlok.exception.ApplicationException;
import com.swadlok.service.FileService;
import com.swadlok.utility.AppUtil;
import com.swadlok.utility.ImageCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.swadlok.utility.AppConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Override
    public FileUploadResponse storeFile(ImageCategory imageCategory, MultipartFile file) {

        log.info("Starting file upload [CATEGORY: {}]", imageCategory);

        // Validate file presence
        if (file == null || file.isEmpty()) {
            log.warn("Uploaded file is null or empty [CATEGORY: {}]", imageCategory);
            throw new IllegalArgumentException("File is empty or missing");
        }

        // Validate file size
        if (file.getSize() > MAX_IMAGE_SIZE) {
            log.warn("File too large [SIZE: {} bytes] [MAX_ALLOWED: {} bytes]", file.getSize(), MAX_IMAGE_SIZE);
            throw new IllegalArgumentException("File size exceeds the maximum limit of " + (MAX_IMAGE_SIZE / (1024 * 1024)) + "MB");
        }

        // Validate file name
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            log.warn("Original filename is null [CATEGORY: {}]", imageCategory);
            throw new IllegalArgumentException("Original file name is missing");
        }

        // Validate extension
        String extension = AppUtil.getExtension(originalFileName);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            log.warn("Invalid file extension [EXTENSION: {}] [ALLOWED: {}]", extension, ALLOWED_IMAGE_EXTENSIONS);
            throw new IllegalArgumentException("Invalid file extension. Allowed: " + ALLOWED_IMAGE_EXTENSIONS);
        }

        // Generate unique, normalized file name
        String randomString = AppUtil.generateRandomString(UPPERCASE_CHARACTERS + LOWERCASE_CHARACTERS, 6);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String prefix = imageCategory.name().toLowerCase();

        String uniqueFileName = prefix + "-" + timestamp + "-" + randomString + "." + extension;
        log.info("Generated unique filename [FILE: {}]", uniqueFileName);

        // Get directory from imageCategory enum
        Path directory = imageCategory.getDirectory();

        try {
            // Ensure directory exists
            Files.createDirectories(directory);
            log.debug("Ensured directory exists [PATH: {}]", directory.toAbsolutePath());

            // Resolve file path
            Path targetPath = directory.resolve(uniqueFileName);
            log.debug("Resolved file target path [PATH: {}]", targetPath.toAbsolutePath());

            // Save file to filesystem
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File stored successfully [FILE: {}] [PATH: {}]", uniqueFileName, targetPath.toAbsolutePath());

            // Return response
            return new FileUploadResponse(
                    uniqueFileName
            );

        } catch (IOException e) {
            log.error("File storage failed [FILE: {}] [ERROR: {}]", uniqueFileName, e.getMessage(), e);
            throw new ApplicationException("Failed to store file: " + e.getMessage(), e);
        }

    }

    @Override
    public void deleteFile(ImageCategory imageCategory, String fileId) {
        Path directory = imageCategory.getDirectory();
        Path targetPath = directory.resolve(fileId);

        try {
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
                log.info("Deleted file [FILE: {}] [CATEGORY: {}] [PATH: {}]", fileId, imageCategory, targetPath.toAbsolutePath());
            } else {
                log.warn("File not found for deletion [FILE: {}] [CATEGORY: {}] [PATH: {}]", fileId, imageCategory, targetPath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Error deleting file [FILE: {}] [ERROR: {}]", fileId, e.getMessage(), e);
        }
    }
}
