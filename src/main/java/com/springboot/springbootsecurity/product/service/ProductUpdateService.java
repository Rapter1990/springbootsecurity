package com.springboot.springbootsecurity.product.service;

import com.springboot.springbootsecurity.product.model.Product;
import com.springboot.springbootsecurity.product.model.dto.request.ProductUpdateRequest;

public interface ProductUpdateService {

    Product updateProductById(final String productId, final ProductUpdateRequest productUpdateRequest);

}
