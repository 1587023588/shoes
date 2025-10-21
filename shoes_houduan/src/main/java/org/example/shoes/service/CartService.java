package org.example.shoes.service;

import java.util.List;

import org.example.shoes.entity.CartItem;
import org.example.shoes.entity.Product;
import org.example.shoes.entity.User;
import org.example.shoes.repository.CartItemRepository;
import org.example.shoes.repository.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("!chat")
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> list(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Transactional
    public CartItem add(User user, Long productId, int quantity) {
        Product p = productRepository.findById(productId).orElseThrow();
        CartItem item = cartItemRepository.findByUserAndProduct(user, p).orElse(null);
        if (item == null) {
            item = new CartItem();
            item.setUser(user);
            item.setProduct(p);
            item.setQuantity(quantity);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        return cartItemRepository.save(item);
    }

    @Transactional
    public CartItem updateQuantity(Long itemId, int quantity, User user) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        if (!item.getUser().getId().equals(user.getId()))
            throw new RuntimeException("无权操作");
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void remove(Long itemId, User user) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        if (!item.getUser().getId().equals(user.getId()))
            throw new RuntimeException("无权操作");
        cartItemRepository.delete(item);
    }
}
