package com.example.music_store.repository;

import com.example.music_store.entity.PurchaseOrder;
import com.example.music_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByUser(User user);
}
