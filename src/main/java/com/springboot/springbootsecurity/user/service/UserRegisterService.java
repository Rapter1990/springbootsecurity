package com.springboot.springbootsecurity.user.service;


import com.springboot.springbootsecurity.user.model.User;
import com.springboot.springbootsecurity.user.model.dto.request.UserRegisterRequest;

public interface UserRegisterService {

    User registerUser(final UserRegisterRequest userRegisterRequest);

}
