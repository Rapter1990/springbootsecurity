package com.springboot.springbootsecurity.admin.service;

import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.LoginRequest;

public interface AdminLoginService {

    Token login(final LoginRequest loginRequest);

}
