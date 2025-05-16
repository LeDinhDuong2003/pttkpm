package com.example.database_service.repository;

import com.example.database_service.model.TapDuLieuMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TapDuLieuMauRepository extends JpaRepository<TapDuLieuMau, Long> {
    List<TapDuLieuMau> findByTapDuLieuId(Long tapDuLieuId);
//    List<TapDuLieuMau> findByMauBaoLucId(Long mauBaoLucId);
    void deleteByTapDuLieuIdAndMauBaoLucId(Long tapDuLieuId, Long mauBaoLucId);
    void deleteByMauBaoLucId(Long mauBaoLucId);
    boolean existsByTapDuLieuIdAndMauBaoLucId(Long tapDuLieuId, Long mauBaoLucId);
}