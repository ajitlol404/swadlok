package com.swadlok.controller;

import com.swadlok.dto.ProfileDto;
import com.swadlok.dto.ProfileDto.Response;
import com.swadlok.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.swadlok.utility.AppConstant.BASE_API_PATH;

@RestController
@RequestMapping(BASE_API_PATH + "/profile")
@RequiredArgsConstructor
public class ProfileRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication.getName()));
    }

    @PatchMapping
    public ResponseEntity<Response> updateProfile(
            Authentication authentication,
            @RequestBody ProfileDto.Request request
    ) {
        return ResponseEntity.ok(userService.updateSuperAdminProfile(authentication.getName(), request));
    }

}
