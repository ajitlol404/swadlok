package com.swadlok.controller;

import com.swadlok.dto.FileUploadResponse;
import com.swadlok.dto.UserDto.AdminRequest;
import com.swadlok.dto.UserDto.AdminResponse;
import com.swadlok.service.FileService;
import com.swadlok.service.UserService;
import com.swadlok.utility.ImageCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.swadlok.utility.AppConstant.BASE_API_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_API_PATH + "/setup")
public class SetupRestController {

    private final UserService userService;
    private final FileService fileService;

    @PostMapping("/users")
    public ResponseEntity<AdminResponse> createUser(@Valid @RequestBody AdminRequest adminRequest) {
        return ResponseEntity.status(CREATED).body(userService.createAdminUser(adminRequest));
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadUserImage(@RequestParam ImageCategory imageCategory, @RequestParam MultipartFile file) {
        return ResponseEntity.status(CREATED).body(fileService.storeFile(imageCategory, file));
    }

}
