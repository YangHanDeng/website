package com.example.security.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/newOrder")
    public ResponseEntity newOrder(@RequestHeader(value="authorization") String auth, @RequestBody OrderRequest orderRequest){
        System.out.println(orderRequest + " "+ auth);
        orderService.addOrder(auth, orderRequest);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/MyOrder")
    public ResponseEntity<OrderResponse> getUserOrder(@RequestHeader(value="authorization") String auth){
        return ResponseEntity.ok(orderService.getUserOrder(auth));
    }
}
