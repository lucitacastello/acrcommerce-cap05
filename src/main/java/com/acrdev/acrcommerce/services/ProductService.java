package com.acrdev.acrcommerce.services;

import com.acrdev.acrcommerce.dto.CategoryDTO;
import com.acrdev.acrcommerce.dto.ProductMinDTO;
import com.acrdev.acrcommerce.entities.Category;
import com.acrdev.acrcommerce.services.exceptions.DatabaseException;
import com.acrdev.acrcommerce.dto.ProductDTO;
import com.acrdev.acrcommerce.entities.Product;
import com.acrdev.acrcommerce.repositories.ProductRepository;
import com.acrdev.acrcommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!")); //.get(); //busca no DB
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
        Page<Product> products = repository.searchByName(name, pageable);
        return products.map(recordFound -> new ProductMinDTO(recordFound));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        try{
            Product entity = repository.getReferenceById(id); //não vai no DB, obj monitorado pela JPA
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
         } catch (EntityNotFoundException e){
            throw  new ResourceNotFoundException("Recurso não encontrado!");
        }


    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }


    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());
        //limpando categorias
        entity.getCategories().clear();
        for(CategoryDTO catDTO : dto.getCategories()){
            Category cat = new Category();
            cat.setId(catDTO.getId()); //copiando o ID do DTO
            entity.getCategories().add(cat); //inserindo na entity - product
        }
    }
}
