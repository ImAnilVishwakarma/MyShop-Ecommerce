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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
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
            throw new BadRequestException("CategoryId id Not found", HttpStatus.SERVICE_UNAVAILABLE);
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

    @Override
    public String saveImage(MultipartFile file) throws IOException, MissingServletRequestPartException {
            if (file == null || file.isEmpty()) {
                throw new MissingServletRequestPartException("file' is not present");
            }

            // Folder ka path
            Path uploadPath = Paths.get("D:\\Anil\\MyShop-Ecommerce\\category-service\\uploads")
                    .toAbsolutePath()
                    .normalize();

            // Folder nahi hai toh create karo
            Files.createDirectories(uploadPath);

            // Unique filename generate karo
            String originalName = file.getOriginalFilename();
            String extension = "";

            if (originalName != null && originalName.lastIndexOf('.') >= 0) {
                extension = originalName.substring(originalName.lastIndexOf('.'));
            }

            String fileName = UUID.randomUUID() + extension;

            // Actual image save karo
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath);

            // Image ka path return karo
            return "/uploads/" + fileName;
        }
    }


