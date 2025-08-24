package com.swadlok.service;

import com.swadlok.dto.UserDto.AdminRequest;
import com.swadlok.dto.UserDto.AdminResponse;

public interface UserService {

    boolean areThereAdminUsers();

    AdminResponse createAdminUser(AdminRequest adminRequest);

}
