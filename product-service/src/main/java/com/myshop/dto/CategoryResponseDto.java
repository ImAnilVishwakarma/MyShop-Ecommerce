
package com.myshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponseDto {

    private Integer categoryId;
    private String categoryName;
    private String description;
    private String originalImgName;
    private String imageUrl;
    private Boolean isActive;

    public ProductDto saveProduct(ProductDto productDto) {
        return productDto;
    }
}
