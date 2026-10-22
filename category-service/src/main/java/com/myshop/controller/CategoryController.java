package com.myshop.controller;

import com.myshop.dto.CategoryRequestDto;
import com.myshop.repository.CategoryRepository;
import com.myshop.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save")
    ResponseEntity<CategoryRequestDto> saveCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
       CategoryRequestDto saved = categoryService.saveCategory(categoryRequestDto);
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
