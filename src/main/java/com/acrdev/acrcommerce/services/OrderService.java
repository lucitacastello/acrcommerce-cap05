package com.acrdev.acrcommerce.services;

import com.acrdev.acrcommerce.dto.OrderDTO;
import com.acrdev.acrcommerce.dto.OrderItemDTO;
import com.acrdev.acrcommerce.entities.*;
import com.acrdev.acrcommerce.repositories.OrderItemRepository;
import com.acrdev.acrcommerce.repositories.OrderRepository;
import com.acrdev.acrcommerce.repositories.ProductRepository;
import com.acrdev.acrcommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    //pegar o User logado
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {

        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!"));
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {

        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        //usuário logado
        User user = userService.authenticated();
        order.setClient(user);
        //iterando todos os itens que vieram na requisição
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem orderItem = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(orderItem); //associado
        }
        repository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);
    }
}
