package com.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
        private Integer productId;
        private String productName;
        private String description;
        private BigDecimal price;
        private Integer quantity;
        private Integer categoryId;
        private Integer brandId;

    }

