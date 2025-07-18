package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
