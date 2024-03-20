package com.springboot.springbootsecurity.admin.service.impl;

import com.springboot.springbootsecurity.admin.exception.AdminNotFoundException;
import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.admin.repository.AdminRepository;
import com.springboot.springbootsecurity.admin.service.AdminLoginService;
import com.springboot.springbootsecurity.auth.exception.PasswordNotValidException;
import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.LoginRequest;
import com.springboot.springbootsecurity.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLoginServiceImpl implements AdminLoginService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public Token login(LoginRequest loginRequest) {

        final AdminEntity adminEntityFromDB = adminRepository
                .findAdminEntityByEmail(loginRequest.getEmail())
                .orElseThrow(
                        () -> new AdminNotFoundException("Can't find with given email: "
                                + loginRequest.getEmail())
                );

        if (Boolean.FALSE.equals(passwordEncoder.matches(
                loginRequest.getPassword(), adminEntityFromDB.getPassword()))) {
            throw new PasswordNotValidException();
        }

        return tokenService.generateToken(adminEntityFromDB.getClaims());

    }

}
