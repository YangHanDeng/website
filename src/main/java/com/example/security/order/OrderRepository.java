package com.example.security.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.email=?1")
    Optional<List<Order>> findOrderByEmail(String email);
    //@Query("SELECT o.homeTeam, o.awayTeam, o.Type, o.subType, o.amount, o.id FROM Order o WHERE o.email=?1")
    //Optional<List<OrderHistory>> findPurchaseHistoryByEmail(String email);
}
