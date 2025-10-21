package org.example.shoes.controller;

import java.util.List;

import org.example.shoes.dto.CartDtos;
import org.example.shoes.entity.CartItem;
import org.example.shoes.entity.User;
import org.example.shoes.service.CartService;
import org.example.shoes.util.CurrentUser;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!chat")
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CurrentUser currentUser;

    public CartController(CartService cartService, CurrentUser currentUser) {
        this.cartService = cartService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public List<CartItem> list() {
        User u = currentUser.requireUser();
        return cartService.list(u);
    }

    @PostMapping("/items")
    public CartItem add(@Validated @RequestBody CartDtos.AddItemRequest req) {
        User u = currentUser.requireUser();
        return cartService.add(u, req.productId, req.quantity);
    }

    @PatchMapping("/items/{id}")
    public CartItem update(@PathVariable Long id, @Validated @RequestBody CartDtos.UpdateItemRequest req) {
        User u = currentUser.requireUser();
        return cartService.updateQuantity(id, req.quantity, u);
    }

    @DeleteMapping("/items/{id}")
    public void delete(@PathVariable Long id) {
        User u = currentUser.requireUser();
        cartService.remove(id, u);
    }
}
