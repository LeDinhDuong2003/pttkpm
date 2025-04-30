package com.example.database_service.repository;


import com.example.database_service.model.LoaiMoHinh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiMoHinhRepository extends JpaRepository<LoaiMoHinh, Long> {
    Page<LoaiMoHinh> findByTenContaining(String ten, Pageable pageable);
}
