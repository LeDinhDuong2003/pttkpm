package com.example.database_service.controller;


import com.example.database_service.model.TapDuLieu;
import com.example.database_service.model.TapDuLieuMau;
import com.example.database_service.service.TapDuLieuMauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tap-du-lieu-mau")
public class TapDuLieuMauController {

    @Autowired
    private TapDuLieuMauService tapDuLieuMauService;

    @GetMapping("/tap-du-lieu/{tapDuLieuId}")
    public ResponseEntity<List<TapDuLieuMau>> layTapDuLieuMauTheoTapDuLieu(@PathVariable("tapDuLieuId") Long tapDuLieuId) {
        try {
            List<TapDuLieuMau> tapDuLieuMaus = tapDuLieuMauService.layTapDuLieuMauTheoTapDuLieu(tapDuLieuId);

            if (tapDuLieuMaus.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tapDuLieuMaus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/mau-bao-luc/{mauBaoLucId}")
//    public ResponseEntity<List<TapDuLieuMau>> layTapDuLieuMauTheoMauBaoLuc(@PathVariable("mauBaoLucId") Long mauBaoLucId) {
//        try {
//            List<TapDuLieuMau> tapDuLieuMaus = tapDuLieuMauService.layTapDuLieuMauTheoMauBaoLuc(mauBaoLucId);
//
//            if (tapDuLieuMaus.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//            return new ResponseEntity<>(tapDuLieuMaus, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @GetMapping("/mau-bao-luc/{mauBaoLucId}/tap-du-lieu")
//    public ResponseEntity<List<TapDuLieu>> layDanhSachTapDuLieuChuaMauBaoLuc(@PathVariable("mauBaoLucId") Long mauBaoLucId) {
//        try {
//            List<TapDuLieu> tapDuLieus = tapDuLieuMauService.layDanhSachTapDuLieuChuaMauBaoLuc(mauBaoLucId);
//
//            if (tapDuLieus.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//            return new ResponseEntity<>(tapDuLieus, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/tap-du-lieu/{tapDuLieuId}/mau-bao-luc/{mauBaoLucId}")
    public ResponseEntity<TapDuLieuMau> themMauVaoTapDuLieu(
            @PathVariable("tapDuLieuId") Long tapDuLieuId,
            @PathVariable("mauBaoLucId") Long mauBaoLucId) {
        try {
            TapDuLieuMau tapDuLieuMau = tapDuLieuMauService.themMauVaoTapDuLieu(tapDuLieuId, mauBaoLucId);

            if (tapDuLieuMau == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(tapDuLieuMau, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tap-du-lieu/{tapDuLieuId}/mau-bao-luc/{mauBaoLucId}")
    public ResponseEntity<HttpStatus> xoaMauKhoiTapDuLieu(
            @PathVariable("tapDuLieuId") Long tapDuLieuId,
            @PathVariable("mauBaoLucId") Long mauBaoLucId) {
        try {
            tapDuLieuMauService.xoaMauKhoiTapDuLieu(tapDuLieuId, mauBaoLucId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
