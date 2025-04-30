package com.example.database_service.repository;


import com.example.database_service.model.MauBaoLuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MauBaoLucRepository extends JpaRepository<MauBaoLuc, Long> {
    List<MauBaoLuc> findByNhan(String nhan);
    Page<MauBaoLuc> findByNhanContaining(String nhan, Pageable pageable);
}
