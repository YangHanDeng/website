package com.example.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // we don't need @Query here because Spring can **automatically generate queries from the method names**
    // User has field email, so Spring can know what findByEmail means
    // damn it's so smart, wtf.
    Optional<User> findByEmail(String email);

    Optional<User> findById(Integer id);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);
}
