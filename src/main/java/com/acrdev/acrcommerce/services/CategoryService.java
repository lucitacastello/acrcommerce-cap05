package com.acrdev.acrcommerce.services;

import com.acrdev.acrcommerce.dto.CategoryDTO;
import com.acrdev.acrcommerce.entities.Category;
import com.acrdev.acrcommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<CategoryDTO> findAll(){

        List<Category> list = repository.findAll();

        return list.stream().map(x -> new CategoryDTO(x)).toList();
       // return list.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
}
