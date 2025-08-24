package com.swadlok.service;

import com.swadlok.dto.FileUploadResponse;
import com.swadlok.utility.ImageCategory;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileUploadResponse storeFile(ImageCategory imageCategory, MultipartFile file);

    void deleteFile(ImageCategory imageCategory, String fileId);

}

