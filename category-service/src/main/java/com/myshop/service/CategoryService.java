package com.myshop.service;


import com.myshop.dto.CategoryRequestDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

 CategoryRequestDto saveCategory(CategoryRequestDto categoryRequestDto);
 CategoryRequestDto updateCategory(Integer id, CategoryRequestDto categoryRequestDto);
 CategoryRequestDto deleteCategory(Integer id, CategoryRequestDto categoryRequestDto);
 CategoryRequestDto getSingleCategory(Integer id);
 List<CategoryRequestDto> getAllCategory();
 String saveImage(MultipartFile file) throws IOException, MissingServletRequestPartException;
}
