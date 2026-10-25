
package com.myshop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myshop.dto.ProductDto;
import com.myshop.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log =
            LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product
    @PostMapping("/save")
    public ResponseEntity<ProductDto> saveProduct(
            @RequestBody ProductDto ProductDto) {

        log.info("Product save request received");

        ProductDto savedProduct =
                productService.saveProduct(ProductDto);

        log.info("Product saved successfully. ID: {}",
                savedProduct.getProductId());

        return new ResponseEntity<>(
                savedProduct, HttpStatus.CREATED);
    }

    // Get All Products
    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts() {

        log.info("Fetching all products");

        List<ProductDto> products =
                productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    // Get Product By ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable Integer id) {

        log.info("Fetching product with ID: {}", id);

        ProductDto product =
                productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductDto proDto) {

        log.info("Updating product with ID: {}", id);

        ProductDto updatedProduct =
                productService.updateProduct(id, proDto);

        return ResponseEntity.ok(updatedProduct);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Integer id) {

        log.info("Deleting product with ID: {}", id);

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
