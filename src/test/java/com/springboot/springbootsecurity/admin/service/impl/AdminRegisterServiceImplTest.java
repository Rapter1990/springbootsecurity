package com.springboot.springbootsecurity.admin.service.impl;

import com.springboot.springbootsecurity.admin.exception.AdminAlreadyExistException;
import com.springboot.springbootsecurity.admin.model.Admin;
import com.springboot.springbootsecurity.admin.model.dto.request.AdminRegisterRequest;
import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.admin.model.mapper.AdminEntityToAdminMapper;
import com.springboot.springbootsecurity.admin.model.mapper.AdminRegisterRequestToAdminEntityMapper;
import com.springboot.springbootsecurity.admin.repository.AdminRepository;
import com.springboot.springbootsecurity.base.AbstractBaseServiceTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminRegisterServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private AdminRegisterServiceImpl adminRegisterService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AdminRegisterRequestToAdminEntityMapper adminRegisterRequestToAdminEntityMapper = AdminRegisterRequestToAdminEntityMapper.initialize();

    private AdminEntityToAdminMapper adminEntityToAdminMapper = AdminEntityToAdminMapper.initialize();


    @Test
    void givenAdminRegisterRequest_whenRegisterAdmin_thenReturnAdmin() {

        // Given
        AdminRegisterRequest request = AdminRegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .build();

        String encodedPassword = "encodedPassword";

        AdminEntity adminEntity = adminRegisterRequestToAdminEntityMapper.mapForSaving(request);

        Admin expected = adminEntityToAdminMapper.map(adminEntity);

        // When
        when(adminRepository.existsAdminEntityByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(adminRepository.save(any(AdminEntity.class))).thenReturn(adminEntity);

        // Then
        Admin result = adminRegisterService.registerAdmin(request);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expected.getFirstName(), result.getFirstName());
        assertEquals(expected.getLastName(), result.getLastName());

        // Verify
        verify(adminRepository).save(any(AdminEntity.class));

    }

    @Test
    void givenAdminRegisterRequest_whenEmailAlreadyExists_thenThrowAdminAlreadyExistException() {

        // Given
        AdminRegisterRequest request = AdminRegisterRequest.builder()
                .email("existing@example.com")
                .password("password123")
                .firstName("Jane")
                .lastName("Doe")
                .phoneNumber("9876543210")
                .build();

        // When
        when(adminRepository.existsAdminEntityByEmail(request.getEmail())).thenReturn(true);

        // Then
        assertThrows(AdminAlreadyExistException.class, () -> adminRegisterService.registerAdmin(request));

        // Verify
        verify(adminRepository, never()).save(any(AdminEntity.class));

    }


}