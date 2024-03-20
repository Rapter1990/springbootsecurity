package com.springboot.springbootsecurity.builder;

import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.auth.model.enums.UserStatus;
import com.springboot.springbootsecurity.auth.model.enums.UserType;

import java.util.UUID;

public class AdminEntityBuilder extends BaseBuilder<AdminEntity> {

    public AdminEntityBuilder() {
        super(AdminEntity.class);
    }


    public AdminEntityBuilder withValidFields() {
        return this
                .withId(UUID.randomUUID().toString())
                .withEmail("example@example.com")
                .withPassword("password")
                .withFirstName("John")
                .withLastName("Doe")
                .withPhoneNumber("1234567890")
                .withUserType(UserType.ADMIN)
                .withUserStatus(UserStatus.ACTIVE);
    }

    public AdminEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminEntityBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public AdminEntityBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public AdminEntityBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AdminEntityBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AdminEntityBuilder withPhoneNumber(String phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AdminEntityBuilder withUserType(UserType userType) {
        data.setUserType(userType);
        return this;
    }

    public AdminEntityBuilder withUserStatus(UserStatus userStatus) {
        data.setUserStatus(userStatus);
        return this;
    }

}
