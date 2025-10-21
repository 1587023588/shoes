package org.example.shoes.controller;

import java.util.List;

import org.example.shoes.entity.Order;
import org.example.shoes.entity.User;
import org.example.shoes.service.OrderService;
import org.example.shoes.util.CurrentUser;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!chat")
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final CurrentUser currentUser;

    public OrderController(OrderService orderService, CurrentUser currentUser) {
        this.orderService = orderService;
        this.currentUser = currentUser;
    }

    @PostMapping
    public Order create() {
        User u = currentUser.requireUser();
        return orderService.createFromCart(u);
    }

    @GetMapping
    public List<Order> list() {
        User u = currentUser.requireUser();
        return orderService.list(u);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        User u = currentUser.requireUser();
        return orderService.get(id, u);
    }

    @PostMapping("/{id}/pay")
    public Order pay(@PathVariable Long id) {
        User u = currentUser.requireUser();
        return orderService.pay(id, u);
    }
}
