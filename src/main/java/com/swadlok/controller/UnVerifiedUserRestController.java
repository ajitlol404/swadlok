package com.swadlok.controller;

import com.swadlok.dto.MessageResponseDto;
import com.swadlok.dto.UnVerifiedUserDto.PasswordRequest;
import com.swadlok.dto.UnVerifiedUserDto.PublicResponse;
import com.swadlok.dto.UnVerifiedUserDto.Request;
import com.swadlok.service.UnVerifiedUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

import static com.swadlok.utility.AppConstant.BASE_API_PATH;

@RestController
@RequestMapping(BASE_API_PATH + "/unverified-users")
@RequiredArgsConstructor
public class UnVerifiedUserRestController {

    private final UnVerifiedUserService unVerifiedUserService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<PublicResponse> createUnverifiedUser(@RequestBody @Valid Request unVerifiedUserRequestDTO) {
        PublicResponse unVerifiedUserPublicResponseDTO = unVerifiedUserService.registerUnVerifiedUser(unVerifiedUserRequestDTO);

        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}/verify")
                                .buildAndExpand(unVerifiedUserPublicResponseDTO.uuid())
                                .toUri()
                )
                .body(unVerifiedUserPublicResponseDTO);
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<PublicResponse> getUnverifiedUser(@PathVariable UUID id) {
        return ResponseEntity.ok(unVerifiedUserService.verifyLink(id));
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<MessageResponseDto> verifyAndSaveUser(@PathVariable UUID id, @Valid @RequestBody PasswordRequest unVerifiedUserPasswordDTO) {
        unVerifiedUserService.verifyAndSaveUser(id, unVerifiedUserPasswordDTO);
        String message = messageSource.getMessage("unverified.user.verification.success", null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new MessageResponseDto(message));
    }

}

