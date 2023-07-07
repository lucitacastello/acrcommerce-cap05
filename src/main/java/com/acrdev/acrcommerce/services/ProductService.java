package com.acrdev.acrcommerce.services;

import com.acrdev.acrcommerce.controllers.handlers.DatabaseException;
import com.acrdev.acrcommerce.dto.ProductDto;
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
    public ProductDto findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!")); //.get(); //busca no DB
        return new ProductDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(String name, Pageable pageable) {
        Page<Product> products = repository.searchByName(name, pageable);
        return products.map(recordFound -> new ProductDto(recordFound));
    }

    @Transactional
    public ProductDto insert(ProductDto dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDto(entity);
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {

        try{
            Product entity = repository.getReferenceById(id); //não vai no DB, obj monitorado pela JPA
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDto(entity);
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


    //forma antiga não funciona
   /* @Transactional(propagation =  Propagation.SUPPORTS)
    public void delete(Long id){
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw  new ResourceNotFoundException("Recurso não encontrado!");
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de Integridade referencial");
        }

    }*/

    private void copyDtoToEntity(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());
    }
}
