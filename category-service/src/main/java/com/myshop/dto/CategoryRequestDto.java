package com.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    private Integer categoryId;
    private String categoryName;
    private String description;
    private String imageUrl;
    private Boolean isActive;
}
