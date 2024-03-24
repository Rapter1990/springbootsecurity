package com.springboot.springbootsecurity.product.service;

import com.springboot.springbootsecurity.common.model.CustomPage;
import com.springboot.springbootsecurity.product.model.Product;
import com.springboot.springbootsecurity.product.model.dto.request.ProductPagingRequest;

public interface ProductReadService {

    Product getProductById(final String productId);

    CustomPage<Product> getProducts(final ProductPagingRequest productPagingRequest);

}
