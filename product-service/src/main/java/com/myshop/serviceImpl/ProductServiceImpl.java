
package com.myshop.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.dto.ProductDto;
import com.myshop.entity.Product;
import com.myshop.exception.ResourceNotFoundException;
import com.myshop.repository.ProductRepository;
import com.myshop.service.ProductService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.myshop.dto.CategoryResponseDto;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;


    public ProductServiceImpl(
            ProductRepository productRepository, RestTemplate restTemplate,
            ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {

        validateCategory(productDto.getCategoryId());

        Product product = modelMapper.map(productDto, Product.class);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDto.class);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Integer id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + id));

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(Integer id, ProductDto proDto) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + id));

        existingProduct.setProductName(proDto.getProductName());
        existingProduct.setDescription(proDto.getDescription());
        existingProduct.setPrice(proDto.getPrice());
        existingProduct.setQuantity(proDto.getQuantity());
        existingProduct.setCategoryId(proDto.getCategoryId());
        existingProduct.setBrandId(proDto.getBrandId());

        Product updatedProduct = productRepository.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(Integer id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + id));

        productRepository.delete(product);
    }

    private void validateCategory(Integer categoryId) {

        if (categoryId == null) {
            throw new IllegalArgumentException(
                    "Category ID is required");
        }

        String url = "http://localhost:8080/api/v1/category/"
                + categoryId;

        try {
            CategoryResponseDto category =
                    restTemplate.getForObject(
                            url, CategoryResponseDto.class);

            if (category == null
                    || category.getCategoryId() == null
                    || !categoryId.equals(category.getCategoryId())) {

                throw new ResourceNotFoundException(
                        "Category not found with ID: " + categoryId);
            }

            if (Boolean.FALSE.equals(category.getIsActive())) {
                throw new IllegalArgumentException(
                        "Category is inactive: " + categoryId);
            }

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException(
                    "Category not found with ID: " + categoryId);
        } catch (RestClientException ex) {
            throw new IllegalStateException(
                    "Unable to communicate with Category Service", ex);
        }
    }

}
