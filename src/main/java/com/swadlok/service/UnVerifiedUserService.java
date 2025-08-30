package com.swadlok.service;

import com.swadlok.dto.UnVerifiedUserDto.PasswordRequest;
import com.swadlok.dto.UnVerifiedUserDto.PublicResponse;
import com.swadlok.dto.UnVerifiedUserDto.Request;

import java.util.UUID;

public interface UnVerifiedUserService {

    PublicResponse registerUnVerifiedUser(Request unVerifiedUserRequestDTO);

    PublicResponse verifyLink(UUID uuid);

    void verifyAndSaveUser(UUID uuid, PasswordRequest unVerifiedUserPasswordDTO);

    void cleanUpExpiredUnverifiedUsers();

}
