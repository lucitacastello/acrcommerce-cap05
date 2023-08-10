package com.acrdev.acrcommerce.repositories;

import com.acrdev.acrcommerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
