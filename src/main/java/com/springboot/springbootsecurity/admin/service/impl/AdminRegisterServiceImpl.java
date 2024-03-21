package com.springboot.springbootsecurity.admin.service.impl;

import com.springboot.springbootsecurity.admin.exception.AdminAlreadyExistException;
import com.springboot.springbootsecurity.admin.model.Admin;
import com.springboot.springbootsecurity.admin.model.dto.request.AdminRegisterRequest;
import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.admin.model.mapper.AdminEntityToAdminMapper;
import com.springboot.springbootsecurity.admin.model.mapper.AdminRegisterRequestToAdminEntityMapper;
import com.springboot.springbootsecurity.admin.repository.AdminRepository;
import com.springboot.springbootsecurity.admin.service.AdminRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminRegisterServiceImpl implements AdminRegisterService {

    private final AdminRepository adminRepository;
    private final AdminRegisterRequestToAdminEntityMapper adminRegisterRequestToAdminEntityMapper = AdminRegisterRequestToAdminEntityMapper.initialize();

    private final AdminEntityToAdminMapper adminEntityToAdminMapper = AdminEntityToAdminMapper.initialize();

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Admin registerAdmin(AdminRegisterRequest adminRegisterRequest) {

        if (adminRepository.existsAdminEntityByEmail(adminRegisterRequest.getEmail())) {
            throw new AdminAlreadyExistException("The email is already used for another admin : " + adminRegisterRequest.getEmail());
        }

        final AdminEntity adminEntityToBeSave = adminRegisterRequestToAdminEntityMapper.mapForSaving(adminRegisterRequest);

        adminEntityToBeSave.setPassword(passwordEncoder.encode(adminRegisterRequest.getPassword()));

        AdminEntity savedAdminEntity = adminRepository.save(adminEntityToBeSave);

        return adminEntityToAdminMapper.map(savedAdminEntity);

    }

}
