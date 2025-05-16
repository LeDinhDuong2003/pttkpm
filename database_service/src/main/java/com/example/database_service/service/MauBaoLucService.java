package com.example.database_service.service;


import com.example.database_service.model.MauBaoLuc;
import com.example.database_service.repository.MauBaoLucRepository;
import com.example.database_service.repository.TapDuLieuMauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MauBaoLucService {

    @Autowired
    private MauBaoLucRepository mauBaoLucRepository;
    @Autowired
    private TapDuLieuMauRepository tapDuLieuMauRepository;

    public Page<MauBaoLuc> layTatCaMauBaoLuc(Pageable pageable) {
        return mauBaoLucRepository.findAll(pageable);
    }

    public Optional<MauBaoLuc> layMauBaoLucTheoId(Long id) {
        return mauBaoLucRepository.findById(id);
    }

    public List<MauBaoLuc> layMauBaoLucTheoNhan(String nhan) {
        return mauBaoLucRepository.findByNhan(nhan);
    }

    public Page<MauBaoLuc> timKiemMauBaoLucTheoNhan(String nhan, Pageable pageable) {
        return mauBaoLucRepository.findByNhanContaining(nhan, pageable);
    }

    public MauBaoLuc luuMauBaoLuc(MauBaoLuc mauBaoLuc) {
        return mauBaoLucRepository.save(mauBaoLuc);
    }

    public void xoaMauBaoLuc(Long id) {
        System.out.println("Xóa mẫu bạo lực với ID: " + id);
//        tapDuLieuMauRepository.deleteByMauBaoLucId(id);
        mauBaoLucRepository.deleteById(id);
    }
}
