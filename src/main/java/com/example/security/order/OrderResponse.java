package com.example.security.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class OrderResponse {
    private String name;
    private List<Order> purchaseHistory;
}
