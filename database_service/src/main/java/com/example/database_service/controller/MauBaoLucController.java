package com.example.database_service.controller;


import com.example.database_service.model.MauBaoLuc;
import com.example.database_service.service.MauBaoLucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/mau-bao-luc")
public class MauBaoLucController {
    @Autowired
    private MauBaoLucService mauBaoLucService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> layDanhSachMauBaoLuc(
            @RequestParam(required = false) String nhan,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int soLuong) {

        try {
            String sapXepTheo = "id";
            String huongSapXep = "asc";
            Sort.Direction huong = "desc".equalsIgnoreCase(huongSapXep) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable paging = PageRequest.of(trang, soLuong, Sort.by(huong, sapXepTheo));

            Page<MauBaoLuc> pageMauBaoLuc;

            if (nhan != null && !nhan.isEmpty()) {
                pageMauBaoLuc = mauBaoLucService.timKiemMauBaoLucTheoNhan(nhan, paging);
            } else {
                pageMauBaoLuc = mauBaoLucService.layTatCaMauBaoLuc(paging);
            }

            List<MauBaoLuc> mauBaoLucs = pageMauBaoLuc.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("mauBaoLucs", mauBaoLucs);
            response.put("trangHienTai", pageMauBaoLuc.getNumber());
            response.put("tongSoMuc", pageMauBaoLuc.getTotalElements());
            response.put("tongSoTrang", pageMauBaoLuc.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MauBaoLuc> layMauBaoLucTheoId(@PathVariable("id") Long id) {
        Optional<MauBaoLuc> mauBaoLucData = mauBaoLucService.layMauBaoLucTheoId(id);

        if (mauBaoLucData.isPresent()) {
            return new ResponseEntity<>(mauBaoLucData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/nhan/{nhan}")
    public ResponseEntity<List<MauBaoLuc>> layMauBaoLucTheoNhan(@PathVariable("nhan") String nhan) {
        try {
            List<MauBaoLuc> mauBaoLucs = mauBaoLucService.layMauBaoLucTheoNhan(nhan);

            if (mauBaoLucs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(mauBaoLucs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<MauBaoLuc> taoMauBaoLuc(
            @RequestBody MauBaoLuc mauBaoLuc) {
        try {
            MauBaoLuc _mauBaoLuc = mauBaoLucService.luuMauBaoLuc(mauBaoLuc);
            return new ResponseEntity<>(_mauBaoLuc, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MauBaoLuc> capNhatMauBaoLuc(
            @PathVariable("id") Long id,
            @RequestBody MauBaoLuc mauBaoLuc) {
        Optional<MauBaoLuc> mauBaoLucData = mauBaoLucService.layMauBaoLucTheoId(id);

        if (mauBaoLucData.isPresent()) {
            MauBaoLuc _mauBaoLuc = mauBaoLucData.get();
            // Tiếp tục MauBaoLucController.java
            _mauBaoLuc.setTen(mauBaoLuc.getTen());
            _mauBaoLuc.setMoTa(mauBaoLuc.getMoTa());
            _mauBaoLuc.setDuongDanVideo(mauBaoLuc.getDuongDanVideo());
            _mauBaoLuc.setNhan(mauBaoLuc.getNhan());

            return new ResponseEntity<>(mauBaoLucService.luuMauBaoLuc(_mauBaoLuc), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> xoaMauBaoLuc(@PathVariable("id") Long id) {
        try {
            mauBaoLucService.xoaMauBaoLuc(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
