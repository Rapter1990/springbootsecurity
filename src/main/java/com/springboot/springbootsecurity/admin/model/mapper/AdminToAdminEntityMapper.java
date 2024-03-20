package com.springboot.springbootsecurity.admin.model.mapper;

import com.springboot.springbootsecurity.admin.model.Admin;
import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminToAdminEntityMapper extends BaseMapper<Admin, AdminEntity> {

    @Override
    AdminEntity map(Admin source);

    static AdminToAdminEntityMapper initialize() {
        return Mappers.getMapper(AdminToAdminEntityMapper.class);
    }

}
