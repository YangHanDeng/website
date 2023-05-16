package com.example.security.order;

import com.example.security.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/order")
public class OrderController {
    /*
    private final UserService userService;
    @Autowired //so constructor will work
    public UserController(UserService userService) {
        this.userService = userService;
    }
     */
    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService){this.orderService=orderService;}
    @PostMapping("/newOrder")
    public void editUser(@RequestHeader(value="authorization") String auth, @RequestBody OrderRequest orderRequest){
        orderService.addOrder(auth, orderRequest);
    }
}
