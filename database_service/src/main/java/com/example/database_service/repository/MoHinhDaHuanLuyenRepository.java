package com.example.database_service.repository;


import com.example.database_service.model.MoHinhDaHuanLuyen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoHinhDaHuanLuyenRepository extends JpaRepository<MoHinhDaHuanLuyen, Long> {
    List<MoHinhDaHuanLuyen> findByLoaiMoHinhId(Long loaiMoHinhId);
//    Page<MoHinhDaHuanLuyen> findByLoaiMoHinhId(Long loaiMoHinhId, Pageable pageable);
//    List<MoHinhDaHuanLuyen> findByMucDich(String mucDich);
    Page<MoHinhDaHuanLuyen> findByMucDich(String mucDich, Pageable pageable);
    List<MoHinhDaHuanLuyen> findByTrangThai(Integer trangThai);
    Page<MoHinhDaHuanLuyen> findByMucDichAndTrangThai(String mucDich ,Integer trangThai, Pageable pageable);
    Page<MoHinhDaHuanLuyen> findByTrangThai(Integer trangThai, Pageable pageable);
}
