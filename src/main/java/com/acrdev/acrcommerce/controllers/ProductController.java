package com.acrdev.acrcommerce.controllers;

import com.acrdev.acrcommerce.dto.ProductDto;
import com.acrdev.acrcommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity <ProductDto> findById(@PathVariable Long id) {
         ProductDto dto = service.findById(id);
         return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll(
            @RequestParam (name = "name", defaultValue = "" ) String name,
            Pageable pageable){
        Page<ProductDto> dto = service.findAll(name, pageable);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity <ProductDto> insert(@Valid @RequestBody  ProductDto dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
