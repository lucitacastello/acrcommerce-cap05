package com.acrdev.acrcommerce.repositories;

import com.acrdev.acrcommerce.entities.OrderItem;
import com.acrdev.acrcommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
