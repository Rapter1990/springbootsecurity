package com.springboot.springbootsecurity.product.service;

import com.springboot.springbootsecurity.product.model.Product;
import com.springboot.springbootsecurity.product.model.dto.request.ProductCreateRequest;

public interface ProductCreateService {

    Product createProduct(final ProductCreateRequest productCreateRequest);

}
