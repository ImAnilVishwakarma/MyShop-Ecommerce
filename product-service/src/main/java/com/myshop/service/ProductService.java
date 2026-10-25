package com.myshop.service;


import com.myshop.dto.ProductDto;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface ProductService {

   public ProductDto saveProduct(ProductDto productDto);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Integer Id);

    ProductDto updateProduct(Integer Id, ProductDto proDto);

    void deleteProduct(Integer Id);

}
