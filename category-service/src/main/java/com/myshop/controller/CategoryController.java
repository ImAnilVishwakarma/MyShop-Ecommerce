package com.myshop.controller;

import com.myshop.dto.CategoryRequestDto;
import com.myshop.repository.CategoryRepository;
import com.myshop.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);


    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryRequestDto> saveCategory(@Valid
            @RequestParam("categoryName") String categoryName,
            @RequestParam("description") String description,
            @RequestParam("isActive") Boolean isActive,
            @RequestParam("file") MultipartFile file) throws IOException, MissingServletRequestPartException {


        log.info("Category save request received: {}", categoryName);

        String imagePath = categoryService.saveImage(file);
        log.info("Image saved successfully: {}", imagePath);
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setCategoryName(categoryName);
        dto.setDescription(description);
        dto.setIsActive(isActive);
        dto.setOriginalImgName(file.getOriginalFilename());
        dto.setImageUrl(imagePath);

        CategoryRequestDto saved = categoryService.saveCategory(dto);
        log.info("Category saved successfully. Category ID: {}",
                saved.getCategoryId());
        log.debug("Category response: {}", saved);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryRequestDto> updateCategory(@PathVariable Integer id, @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryRequestDto saved = categoryService.updateCategory(id, categoryRequestDto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CategoryRequestDto> deleteCategory(@PathVariable Integer id, @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryRequestDto saved = categoryService.deleteCategory(id, categoryRequestDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryRequestDto> getSingleCategory(@PathVariable Integer id) {
        CategoryRequestDto saved = categoryService.getSingleCategory(id);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryRequestDto>> getAllCategory() {
        List<CategoryRequestDto> getAllCategory = categoryService.getAllCategory();
        if (getAllCategory.isEmpty()) {
            System.out.println("there is no category");
        }
        return ResponseEntity.ok(getAllCategory);
        }


}
