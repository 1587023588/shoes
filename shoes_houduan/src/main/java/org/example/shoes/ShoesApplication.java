package org.example.shoes;

import org.example.shoes.entity.Product;
import org.example.shoes.entity.User;
import org.example.shoes.repository.ProductRepository;
import org.example.shoes.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ShoesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoesApplication.class, args);
    }

    @Bean
    @Profile("!chat")
    CommandLineRunner seed(ProductRepository productRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (productRepo.count() == 0) {
                Product p1 = new Product();
                p1.setName("示例球鞋A");
                p1.setSubtitle("舒适透气");
                p1.setPrice(19900);
                p1.setStock(100);
                productRepo.save(p1);

                Product p2 = new Product();
                p2.setName("示例球鞋B");
                p2.setSubtitle("轻盈回弹");
                p2.setPrice(29900);
                p2.setStock(50);
                productRepo.save(p2);
            }

            // 默认用户：test / 123456
            userRepo.findByUsername("test").orElseGet(() -> {
                User u = new User();
                u.setUsername("test");
                u.setPasswordHash(encoder.encode("123456"));
                return userRepo.save(u);
            });
        };
    }
}
