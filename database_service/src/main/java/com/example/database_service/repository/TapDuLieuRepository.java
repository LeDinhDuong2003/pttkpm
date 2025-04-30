package com.example.database_service.repository;


import com.example.database_service.model.TapDuLieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TapDuLieuRepository extends JpaRepository<TapDuLieu, Long> {
    List<TapDuLieu> findByLoai(String loai);
    Page<TapDuLieu> findByLoai(String loai, Pageable pageable);
}
