package com.example.security.order;


import com.example.security.user.User;
import com.example.security.user.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository){
        this.orderRepository=orderRepository;
        this.userRepository = userRepository;
    }
    private String extractEmailFromAuth(String auth){ //identical to UserService/extract..., future: can integrate into 1
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = auth.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        JSONObject json = new JSONObject(payload);
        return json.getString("sub");
    }
    private boolean validOrder(OrderRequest orderRequest){

        if(orderRequest.getType().equals("winorloss"))
            if(orderRequest.getSubType().equals("win") || orderRequest.getSubType().equals("draw") || orderRequest.getSubType().equals("loss"))
                return true;
        if(orderRequest.getType().equals("bigsmall") || orderRequest.getType().equals("cards"))
            if(orderRequest.getSubType().equals("big")|| orderRequest.getSubType().equals("small"))
                return true;
        if(orderRequest.getType().equals("corners"))
            if(orderRequest.getSubType().equals("home")|| orderRequest.getSubType().equals("same")|| orderRequest.getSubType().equals("away"))
                return true;
        return false;
    }
    public boolean addOrder(String auth, OrderRequest orderRequest){
        String email = extractEmailFromAuth(auth);//verify user
        var user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        System.out.println(orderRequest);
        if(!validOrder(orderRequest)) return false;// verify order
        var order = Order.builder().email(email)
                .homeTeam(orderRequest.getHomeTeam())
                .awayTeam(orderRequest.getAwayTeam())
                .Type(orderRequest.getType())
                .subType(orderRequest.getSubType())
                .amount(orderRequest.getAmount()).build();
        orderRepository.save(order);
        return true;
    }

    public List<Order> getUserOrder(String auth){
        String email = extractEmailFromAuth(auth);//verify user
        User u = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return orderRepository.findOrderByEmail(email).orElse(new ArrayList<>());
    }

}
