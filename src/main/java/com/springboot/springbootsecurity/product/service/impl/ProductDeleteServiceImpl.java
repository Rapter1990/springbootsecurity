package com.springboot.springbootsecurity.product.service.impl;

import com.springboot.springbootsecurity.product.exception.ProductNotFoundException;
import com.springboot.springbootsecurity.product.model.entity.ProductEntity;
import com.springboot.springbootsecurity.product.repository.ProductRepository;
import com.springboot.springbootsecurity.product.service.ProductDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDeleteServiceImpl implements ProductDeleteService {

    private final ProductRepository productRepository;

    @Override
    public void deleteProductById(String productId) {

        final ProductEntity productEntityToBeDelete = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("With given productID = " + productId));

        productRepository.delete(productEntityToBeDelete);

    }

}
