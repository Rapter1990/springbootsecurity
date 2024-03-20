package com.springboot.springbootsecurity.admin.model.mapper;

import com.springboot.springbootsecurity.admin.model.Admin;
import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminEntityToAdminMapper extends BaseMapper<AdminEntity, Admin> {

    @Override
    Admin map(AdminEntity source);

    static AdminEntityToAdminMapper initialize() {
        return Mappers.getMapper(AdminEntityToAdminMapper.class);
    }

}
