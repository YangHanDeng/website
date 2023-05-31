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
        if(orderService.addOrder(auth, orderRequest)) {
            System.out.println("true");
            return ResponseEntity.ok().build();
        }else{
            System.out.println("false");
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/MyOrder")
    public ResponseEntity<List<Order> > getUserOrder(@RequestHeader(value="authorization") String auth){
        return ResponseEntity.ok(orderService.getUserOrder(auth));
    }
}
