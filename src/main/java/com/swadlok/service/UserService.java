package com.swadlok.service;

import com.swadlok.dto.ProfileDto;
import com.swadlok.dto.UserDto.AdminRequest;
import com.swadlok.dto.UserDto.AdminResponse;

public interface UserService {

    boolean areThereAdminUsers();

    AdminResponse createAdminUser(AdminRequest adminRequest);

    boolean userExistsByEmail(String email);

    void createCustomer(String name, String email, String password, String phoneNumber);

    ProfileDto.Response getProfile(String email);

    ProfileDto.Response updateSuperAdminProfile(String email, ProfileDto.Request request);

}
