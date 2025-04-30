package com.example.database_service.service;


import com.example.database_service.model.TapDuLieu;
import com.example.database_service.repository.TapDuLieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TapDuLieuService {

    @Autowired
    private TapDuLieuRepository tapDuLieuRepository;

    public List<TapDuLieu> layTatCaTapDuLieu() {
        return tapDuLieuRepository.findAll();
    }

    public Page<TapDuLieu> layTatCaTapDuLieu(Pageable pageable) {
        return tapDuLieuRepository.findAll(pageable);
    }

    public Optional<TapDuLieu> layTapDuLieuTheoId(Long id) {
        return tapDuLieuRepository.findById(id);
    }

    public List<TapDuLieu> layTapDuLieuTheoLoai(String loai) {
        return tapDuLieuRepository.findByLoai(loai);
    }

    public Page<TapDuLieu> layTapDuLieuTheoLoai(String loai, Pageable pageable) {
        return tapDuLieuRepository.findByLoai(loai, pageable);
    }

    public TapDuLieu luuTapDuLieu(TapDuLieu tapDuLieu) {
        return tapDuLieuRepository.save(tapDuLieu);
    }

    public void xoaTapDuLieu(Long id) {
        tapDuLieuRepository.deleteById(id);
    }
}
