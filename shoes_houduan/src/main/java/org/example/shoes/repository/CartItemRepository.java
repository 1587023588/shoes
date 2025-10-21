package org.example.shoes.repository;

import java.util.List;
import java.util.Optional;

import org.example.shoes.entity.CartItem;
import org.example.shoes.entity.Product;
import org.example.shoes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
}
