package com.myshop.serviceImpl;

import com.myshop.dto.CategoryRequestDto;
import com.myshop.entity.Category;
import com.myshop.exception.BadRequestException;
import com.myshop.exception.ResourceNotFoundException;
import com.myshop.repository.CategoryRepository;
import com.myshop.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repo;
    @Autowired
    private ModelMapper mapper;


    @Override
    public CategoryRequestDto saveCategory(CategoryRequestDto categoryRequestDto) {
        Category category = mapper.map(categoryRequestDto, Category.class);
        Category save = repo.save(category);
        return mapper.map(save, CategoryRequestDto.class);
    }

    @Override
    public CategoryRequestDto updateCategory(Integer id, CategoryRequestDto categoryRequestDto) {
        if (id == null || categoryRequestDto.getCategoryId() == null) {
            throw new BadRequestException("CategoryId is null", HttpStatus.SERVICE_UNAVAILABLE);
        }
        if (!Objects.equals(id, categoryRequestDto.getCategoryId())) {
            throw new BadRequestException("Category ID mismatch :", HttpStatus.SERVICE_UNAVAILABLE);
        }
        Category existcategory = repo.findById(id).orElse(null);

        if (existcategory == null) {
            throw new BadRequestException("Category ID mismatch :", HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            Category category = mapper.map(categoryRequestDto, Category.class);
            category.setCreatedAt(existcategory.getCreatedAt());
            repo.save(category);
            return mapper.map(category, CategoryRequestDto.class);
        }
    }

    @Override
    public CategoryRequestDto deleteCategory(Integer id, CategoryRequestDto categoryRequestDto) {
        if (id == null || categoryRequestDto.getCategoryId() == null) {
            System.out.println("Category ID is required : " + id);
            return categoryRequestDto;
        }
        Category existcategory = repo.findById(id).orElse(null);
        if (existcategory == null) {
            System.out.println("Category not found with id: " + id);
            return categoryRequestDto;
        } else {
            Category category = mapper.map(categoryRequestDto, Category.class);
            repo.delete(category);
            mapper.map(category, CategoryRequestDto.class);
            return mapper.map(category, CategoryRequestDto.class);
        }
    }

    @Override
    public CategoryRequestDto getSingleCategory(Integer id) {
        if (id == null) {
            throw new ResourceNotFoundException("Category ID is required");
        }
        Category existcategory = repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found with id: "));
        if (existcategory == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        return mapper.map(existcategory, CategoryRequestDto.class);
    }

    @Override
    public List<CategoryRequestDto> getAllCategory() {
        List<Category> categoryList = repo.findAll();
        if (categoryList.isEmpty()) {
            throw new ResourceNotFoundException("Category not found");
        }
       List<CategoryRequestDto> categoryRequestDtoList =  categoryList.stream().map(cat->mapper.map(cat, CategoryRequestDto.class)).toList();
       return categoryRequestDtoList;
    }   
}

