package com.springboot.springbootsecurity.product.repository;

import com.springboot.springbootsecurity.product.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    boolean existsProductEntityByName(final String name);

}
