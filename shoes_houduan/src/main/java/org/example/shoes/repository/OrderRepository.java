package org.example.shoes.repository;

import java.util.List;

import org.example.shoes.entity.Order;
import org.example.shoes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
