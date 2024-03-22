package com.springboot.springbootsecurity.user.service;

import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.LoginRequest;

public interface UserLoginService {

    Token login(final LoginRequest loginRequest);

}
