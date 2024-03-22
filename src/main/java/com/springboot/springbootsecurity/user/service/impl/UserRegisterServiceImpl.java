package com.springboot.springbootsecurity.user.service.impl;

import com.springboot.springbootsecurity.user.exception.UserAlreadyExistException;
import com.springboot.springbootsecurity.user.model.User;
import com.springboot.springbootsecurity.user.model.dto.request.UserRegisterRequest;
import com.springboot.springbootsecurity.user.model.entity.UserEntity;
import com.springboot.springbootsecurity.user.model.mapper.UserEntityToUserMapper;
import com.springboot.springbootsecurity.user.model.mapper.UserRegisterRequestToUserEntityMapper;
import com.springboot.springbootsecurity.user.repository.UserRepository;
import com.springboot.springbootsecurity.user.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterServiceImpl implements UserRegisterService {

    private final UserRepository userRepository;

    private final UserRegisterRequestToUserEntityMapper userRegisterRequestToUserEntityMapper = UserRegisterRequestToUserEntityMapper.initialize();

    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();

    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegisterRequest userRegisterRequest) {

        if (userRepository.existsUserEntityByEmail(userRegisterRequest.getEmail())) {
            throw new UserAlreadyExistException("The email is already used for another admin : " + userRegisterRequest.getEmail());
        }

        final UserEntity userEntityToBeSave = userRegisterRequestToUserEntityMapper.mapForSaving(userRegisterRequest);

        userEntityToBeSave.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

        UserEntity savedUserEntity = userRepository.save(userEntityToBeSave);

        return userEntityToUserMapper.map(savedUserEntity);

    }

}
