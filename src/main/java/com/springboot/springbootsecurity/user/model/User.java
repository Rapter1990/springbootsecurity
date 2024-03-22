package com.springboot.springbootsecurity.user.model;

import com.springboot.springbootsecurity.auth.model.enums.UserStatus;
import com.springboot.springbootsecurity.auth.model.enums.UserType;
import com.springboot.springbootsecurity.common.model.BaseDomainModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDomainModel {

    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Builder.Default
    private UserType userType = UserType.USER;

    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

}