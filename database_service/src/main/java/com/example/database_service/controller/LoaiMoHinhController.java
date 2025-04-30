package com.example.database_service.controller;

import com.example.database_service.model.LoaiMoHinh;
import com.example.database_service.service.LoaiMoHinhService;
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
@RequestMapping("/api/loai-mo-hinh")
public class LoaiMoHinhController {

    @Autowired
    private LoaiMoHinhService loaiMoHinhService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> layDanhSachLoaiMoHinh(
            @RequestParam(required = false) String ten,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int soLuong) {

        try {
            String sapXepTheo = "id";
            String huongSapXep = "asc";
            Sort.Direction huong = "desc".equalsIgnoreCase(huongSapXep) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable paging = PageRequest.of(trang, soLuong, Sort.by(huong, sapXepTheo));

            Page<LoaiMoHinh> pageLoaiMoHinh;

            if (ten != null && !ten.isEmpty()) {
                pageLoaiMoHinh = loaiMoHinhService.timKiemLoaiMoHinhTheoTen(ten, paging);
            } else {
                pageLoaiMoHinh = loaiMoHinhService.layTatCaLoaiMoHinh(paging);
            }

            List<LoaiMoHinh> loaiMoHinhs = pageLoaiMoHinh.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("loaiMoHinhs", loaiMoHinhs);
            response.put("trangHienTai", pageLoaiMoHinh.getNumber());
            response.put("tongSoMuc", pageLoaiMoHinh.getTotalElements());
            response.put("tongSoTrang", pageLoaiMoHinh.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<LoaiMoHinh> layLoaiMoHinhTheoId(@PathVariable("id") Long id) {
//        Optional<LoaiMoHinh> loaiMoHinhData = loaiMoHinhService.layLoaiMoHinhTheoId(id);
//
//        if (loaiMoHinhData.isPresent()) {
//            return new ResponseEntity<>(loaiMoHinhData.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

//    @PostMapping
//    public ResponseEntity<LoaiMoHinh> taoLoaiMoHinh(
//            @RequestBody LoaiMoHinh loaiMoHinh) {
//        try {
//
//            LoaiMoHinh _loaiMoHinh = loaiMoHinhService.luuLoaiMoHinh(loaiMoHinh);
//            return new ResponseEntity<>(_loaiMoHinh, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<LoaiMoHinh> capNhatLoaiMoHinh(
//            @PathVariable("id") Long id,
//            @RequestBody LoaiMoHinh loaiMoHinh) {
//        Optional<LoaiMoHinh> loaiMoHinhData = loaiMoHinhService.layLoaiMoHinhTheoId(id);
//
//        if (loaiMoHinhData.isPresent()) {
//            LoaiMoHinh _loaiMoHinh = loaiMoHinhData.get();
//            _loaiMoHinh.setTen(loaiMoHinh.getTen());
//            _loaiMoHinh.setMoTa(loaiMoHinh.getMoTa());
//            _loaiMoHinh.setThongSoMacDinh(loaiMoHinh.getThongSoMacDinh());
//
//            return new ResponseEntity<>(loaiMoHinhService.luuLoaiMoHinh(_loaiMoHinh), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<HttpStatus> xoaLoaiMoHinh(@PathVariable("id") Long id) {
//        try {
//            loaiMoHinhService.xoaLoaiMoHinh(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
