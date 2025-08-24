package com.swadlok.controller;

import com.swadlok.dto.FileUploadResponse;
import com.swadlok.service.FileService;
import com.swadlok.utility.ImageCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.swadlok.utility.AppConstant.BASE_API_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(BASE_API_PATH + "/files")
@RequiredArgsConstructor
public class FileRestController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam MultipartFile file,
            @RequestParam ImageCategory imageCategory
    ) {
        return ResponseEntity.status(CREATED).body(fileService.storeFile(imageCategory, file));
    }

}
