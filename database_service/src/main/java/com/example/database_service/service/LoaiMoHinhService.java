package com.example.database_service.service;


import com.example.database_service.model.LoaiMoHinh;
import com.example.database_service.repository.LoaiMoHinhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoaiMoHinhService {

    @Autowired
    private LoaiMoHinhRepository loaiMoHinhRepository;

    public List<LoaiMoHinh> layTatCaLoaiMoHinh() {
        return loaiMoHinhRepository.findAll();
    }

    public Page<LoaiMoHinh> layTatCaLoaiMoHinh(Pageable pageable) {
        return loaiMoHinhRepository.findAll(pageable);
    }

    public Optional<LoaiMoHinh> layLoaiMoHinhTheoId(Long id) {
        return loaiMoHinhRepository.findById(id);
    }

    public Page<LoaiMoHinh> timKiemLoaiMoHinhTheoTen(String ten, Pageable pageable) {
        return loaiMoHinhRepository.findByTenContaining(ten, pageable);
    }

    public LoaiMoHinh luuLoaiMoHinh(LoaiMoHinh loaiMoHinh) {
        return loaiMoHinhRepository.save(loaiMoHinh);
    }

    public void xoaLoaiMoHinh(Long id) {
        loaiMoHinhRepository.deleteById(id);
    }
}
