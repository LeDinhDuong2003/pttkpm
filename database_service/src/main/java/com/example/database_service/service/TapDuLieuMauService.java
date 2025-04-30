package com.example.database_service.service;


import com.example.database_service.model.MauBaoLuc;
import com.example.database_service.model.TapDuLieu;
import com.example.database_service.model.TapDuLieuMau;
import com.example.database_service.repository.MauBaoLucRepository;
import com.example.database_service.repository.TapDuLieuMauRepository;
import com.example.database_service.repository.TapDuLieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TapDuLieuMauService {

    @Autowired
    private TapDuLieuMauRepository tapDuLieuMauRepository;

    @Autowired
    private TapDuLieuRepository tapDuLieuRepository;

    @Autowired
    private MauBaoLucRepository mauBaoLucRepository;

//    public List<TapDuLieuMau> layTatCaTapDuLieuMau() {
//        return tapDuLieuMauRepository.findAll();
//    }
//
//    public Optional<TapDuLieuMau> layTapDuLieuMauTheoId(Long id) {
//        return tapDuLieuMauRepository.findById(id);
//    }

    public List<TapDuLieuMau> layTapDuLieuMauTheoTapDuLieu(Long tapDuLieuId) {
        return tapDuLieuMauRepository.findByTapDuLieuId(tapDuLieuId);
    }

//    public List<TapDuLieuMau> layTapDuLieuMauTheoMauBaoLuc(Long mauBaoLucId) {
//        return tapDuLieuMauRepository.findByMauBaoLucId(mauBaoLucId);
//    }

    public List<MauBaoLuc> layDanhSachMauBaoLucTrongTapDuLieu(Long tapDuLieuId) {
        List<TapDuLieuMau> tapDuLieuMaus = tapDuLieuMauRepository.findByTapDuLieuId(tapDuLieuId);
        List<MauBaoLuc> mauBaoLucs = new ArrayList<>();

        for (TapDuLieuMau tdm : tapDuLieuMaus) {
            mauBaoLucs.add(tdm.getMauBaoLuc());
        }

        return mauBaoLucs;
    }

//    public List<TapDuLieu> layDanhSachTapDuLieuChuaMauBaoLuc(Long mauBaoLucId) {
//        List<TapDuLieuMau> tapDuLieuMaus = tapDuLieuMauRepository.findByMauBaoLucId(mauBaoLucId);
//        List<TapDuLieu> tapDuLieus = new ArrayList<>();
//
//        for (TapDuLieuMau tdm : tapDuLieuMaus) {
//            tapDuLieus.add(tdm.getTapDuLieu());
//        }
//
//        return tapDuLieus;
//    }

//    public TapDuLieuMau luuTapDuLieuMau(TapDuLieuMau tapDuLieuMau) {
//        return tapDuLieuMauRepository.save(tapDuLieuMau);
//    }

    @Transactional
    public TapDuLieuMau themMauVaoTapDuLieu(Long tapDuLieuId, Long mauBaoLucId) {
        if (tapDuLieuMauRepository.existsByTapDuLieuIdAndMauBaoLucId(tapDuLieuId, mauBaoLucId)) {
            return null; // Mẫu đã tồn tại trong tập dữ liệu
        }

        Optional<TapDuLieu> tapDuLieuOpt = tapDuLieuRepository.findById(tapDuLieuId);
        Optional<MauBaoLuc> mauBaoLucOpt = mauBaoLucRepository.findById(mauBaoLucId);

        if (!tapDuLieuOpt.isPresent() || !mauBaoLucOpt.isPresent()) {
            return null;
        }

        TapDuLieuMau tapDuLieuMau = new TapDuLieuMau();
        tapDuLieuMau.setTapDuLieu(tapDuLieuOpt.get());
        tapDuLieuMau.setMauBaoLuc(mauBaoLucOpt.get());

        return tapDuLieuMauRepository.save(tapDuLieuMau);
    }

    @Transactional
    public void xoaMauKhoiTapDuLieu(Long tapDuLieuId, Long mauBaoLucId) {
        tapDuLieuMauRepository.deleteByTapDuLieuIdAndMauBaoLucId(tapDuLieuId, mauBaoLucId);
    }

    public void xoaTapDuLieuMau(Long id) {
        tapDuLieuMauRepository.deleteById(id);
    }
}