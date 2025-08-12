package com.example.music_store.repository;

import com.example.music_store.entity.Order;
import com.example.music_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
