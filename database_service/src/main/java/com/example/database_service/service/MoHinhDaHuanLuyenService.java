package com.example.database_service.service;


import com.example.database_service.model.MoHinhDaHuanLuyen;
import com.example.database_service.repository.MoHinhDaHuanLuyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MoHinhDaHuanLuyenService {

    @Autowired
    private MoHinhDaHuanLuyenRepository moHinhDaHuanLuyenRepository;

    public List<MoHinhDaHuanLuyen> layTatCaMoHinhDaHuanLuyen() {
        return moHinhDaHuanLuyenRepository.findAll();
    }

    public Page<MoHinhDaHuanLuyen> layTatCaMoHinhDaHuanLuyen(Pageable pageable) {
        return moHinhDaHuanLuyenRepository.findAll(pageable);
    }

    public Optional<MoHinhDaHuanLuyen> layMoHinhDaHuanLuyenTheoId(Long id) {
        return moHinhDaHuanLuyenRepository.findById(id);
    }

    public List<MoHinhDaHuanLuyen> layMoHinhDaHuanLuyenTheoLoaiMoHinh(Long loaiMoHinhId) {
        return moHinhDaHuanLuyenRepository.findByLoaiMoHinhId(loaiMoHinhId);
    }

//    public Page<MoHinhDaHuanLuyen> layMoHinhDaHuanLuyenTheoLoaiMoHinh(Long loaiMoHinhId, Pageable pageable) {
//        return moHinhDaHuanLuyenRepository.findByLoaiMoHinhId(loaiMoHinhId, pageable);
//    }

    public Page<MoHinhDaHuanLuyen> layMoHinhDaHuanLuyenTheoMucDich(String mucDich, Pageable pageable) {
        return moHinhDaHuanLuyenRepository.findByMucDich(mucDich, pageable);
    }

    public List<MoHinhDaHuanLuyen> layMoHinhDaHuanLuyenTheoTrangThai(Integer trangThai) {
        return moHinhDaHuanLuyenRepository.findByTrangThai(trangThai);
    }

    public Page<MoHinhDaHuanLuyen> layMoHinhDaHuanLuyenTheoTrangThaiVaMucDich(String mucDich,Integer trangThai, Pageable pageable) {
        return moHinhDaHuanLuyenRepository.findByMucDichAndTrangThai(mucDich,trangThai, pageable);
    }

    public Page<MoHinhDaHuanLuyen> layMoHinhDaHuanLuyenTheoTrangThai(Integer trangThai, Pageable pageable) {
        return moHinhDaHuanLuyenRepository.findByTrangThai(trangThai, pageable);
    }

    public MoHinhDaHuanLuyen luuMoHinhDaHuanLuyen(MoHinhDaHuanLuyen moHinhDaHuanLuyen) {
        return moHinhDaHuanLuyenRepository.save(moHinhDaHuanLuyen);
    }

    public MoHinhDaHuanLuyen capNhatTrangThai(Long id, Integer trangThai) {
        Optional<MoHinhDaHuanLuyen> moHinhOpt = moHinhDaHuanLuyenRepository.findById(id);
        if (!moHinhOpt.isPresent()) {
            return null;
        }

        MoHinhDaHuanLuyen moHinh = moHinhOpt.get();
        moHinh.setTrangThai(trangThai);

//        if (trangThai == MoHinhDaHuanLuyen.TrangThai.HOAN_THANH || trangThai == MoHinhDaHuanLuyen.TrangThai.LOI) {
            moHinh.setThoiGianKetThuc(LocalDateTime.now());
//        }

        return moHinhDaHuanLuyenRepository.save(moHinh);
    }

    public void xoaMoHinhDaHuanLuyen(Long id) {
        moHinhDaHuanLuyenRepository.deleteById(id);
    }
}
