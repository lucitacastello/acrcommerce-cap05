package com.acrdev.acrcommerce.repositories;

import com.acrdev.acrcommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
