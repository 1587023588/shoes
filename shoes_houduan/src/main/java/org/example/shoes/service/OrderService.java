package org.example.shoes.service;

import java.util.List;

import org.example.shoes.entity.CartItem;
import org.example.shoes.entity.Order;
import org.example.shoes.entity.OrderItem;
import org.example.shoes.entity.Product;
import org.example.shoes.entity.User;
import org.example.shoes.repository.CartItemRepository;
import org.example.shoes.repository.OrderItemRepository;
import org.example.shoes.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Order createFromCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        if (items.isEmpty()) throw new RuntimeException("购物车为空");

        Order order = new Order();
        order.setUser(user);
        order.setStatus("CREATED");
        int total = 0;
        order = orderRepository.save(order);

        for (CartItem ci : items) {
            Product p = ci.getProduct();
            int price = p.getPrice();
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(p);
            oi.setProductName(p.getName());
            oi.setPrice(price);
            oi.setQuantity(ci.getQuantity());
            orderItemRepository.save(oi);
            total += price * ci.getQuantity();
        }
        order.setTotalAmount(total);
        order = orderRepository.save(order);
        cartItemRepository.deleteAll(items);
        return order;
    }

    public List<Order> list(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Order get(Long id, User user) {
        Order o = orderRepository.findById(id).orElseThrow();
        if (!o.getUser().getId().equals(user.getId())) throw new RuntimeException("无权查看");
        return o;
    }

    @Transactional
    public Order pay(Long id, User user) {
        Order o = get(id, user);
        if (!"CREATED".equals(o.getStatus())) throw new RuntimeException("订单状态不可支付");
        // 模拟支付成功
        o.setStatus("PAID");
        return orderRepository.save(o);
    }
}
