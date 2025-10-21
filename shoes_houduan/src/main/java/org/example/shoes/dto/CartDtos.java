package org.example.shoes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartDtos {
    public static class AddItemRequest {
        @NotNull
        public Long productId;
        @NotNull @Min(1)
        public Integer quantity;
    }

    public static class UpdateItemRequest {
        @NotNull @Min(1)
        public Integer quantity;
    }
}
