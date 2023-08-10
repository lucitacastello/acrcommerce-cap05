package com.acrdev.acrcommerce.services;

import com.acrdev.acrcommerce.dto.OrderDTO;
import com.acrdev.acrcommerce.entities.Order;
import com.acrdev.acrcommerce.repositories.OrderRepository;
import com.acrdev.acrcommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {

        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!"));
        return new OrderDTO(order);
    }
}
