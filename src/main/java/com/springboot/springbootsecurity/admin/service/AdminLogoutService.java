package com.springboot.springbootsecurity.admin.service;

import com.springboot.springbootsecurity.auth.model.dto.request.TokenInvalidateRequest;

public interface AdminLogoutService {

    void logout(final TokenInvalidateRequest tokenInvalidateRequest);

}
