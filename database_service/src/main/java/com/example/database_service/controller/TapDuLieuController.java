package com.example.database_service.controller;

import com.example.database_service.model.MauBaoLuc;
import com.example.database_service.model.TapDuLieu;
import com.example.database_service.service.TapDuLieuMauService;
import com.example.database_service.service.TapDuLieuService;
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
@RequestMapping("/api/tap-du-lieu")
public class TapDuLieuController {

    @Autowired
    private TapDuLieuService tapDuLieuService;

    @Autowired
    private TapDuLieuMauService tapDuLieuMauService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> layDanhSachTapDuLieu(
            @RequestParam(required = false) String loai,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int soLuong) {

        try {
            String sapXepTheo = "id";
            String huongSapXep = "asc";
            Sort.Direction huong = "desc".equalsIgnoreCase(huongSapXep) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable paging = PageRequest.of(trang, soLuong, Sort.by(huong, sapXepTheo));

            Page<TapDuLieu> pageTapDuLieu;

            if (loai != null && !loai.isEmpty()) {
                try {
                    String loaiTapDuLieu = loai;
                    pageTapDuLieu = tapDuLieuService.layTapDuLieuTheoLoai(loaiTapDuLieu, paging);
                } catch (IllegalArgumentException e) {
                    pageTapDuLieu = tapDuLieuService.layTatCaTapDuLieu(paging);
                }
            } else {
                pageTapDuLieu = tapDuLieuService.layTatCaTapDuLieu(paging);
            }

            List<TapDuLieu> tapDuLieus = pageTapDuLieu.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("tapDuLieus", tapDuLieus);
            response.put("trangHienTai", pageTapDuLieu.getNumber());
            response.put("tongSoMuc", pageTapDuLieu.getTotalElements());
            response.put("tongSoTrang", pageTapDuLieu.getTotalPages());

//            System.out.println(new ResponseEntity<>(response, HttpStatus.OK).toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TapDuLieu> layTapDuLieuTheoId(@PathVariable("id") Long id) {
        Optional<TapDuLieu> tapDuLieuData = tapDuLieuService.layTapDuLieuTheoId(id);

        if (tapDuLieuData.isPresent()) {
            return new ResponseEntity<>(tapDuLieuData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/loai/{loai}")
//    public ResponseEntity<List<TapDuLieu>> layTapDuLieuTheoLoai(@PathVariable("loai") String loai) {
//        try {
//            String loaiTapDuLieu = loai;
//            List<TapDuLieu> tapDuLieus = tapDuLieuService.layTapDuLieuTheoLoai(loaiTapDuLieu);
//
//            if (tapDuLieus.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//            return new ResponseEntity<>(tapDuLieus, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{id}/mau-bao-luc")
    public ResponseEntity<List<MauBaoLuc>> layDanhSachMauBaoLucTrongTapDuLieu(@PathVariable("id") Long tapDuLieuId) {
        try {
            List<MauBaoLuc> mauBaoLucs = tapDuLieuMauService.layDanhSachMauBaoLucTrongTapDuLieu(tapDuLieuId);

            if (mauBaoLucs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(mauBaoLucs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<TapDuLieu> taoTapDuLieu(
            @RequestBody TapDuLieu tapDuLieu) {
        try {

            TapDuLieu _tapDuLieu = tapDuLieuService.luuTapDuLieu(tapDuLieu);
            return new ResponseEntity<>(_tapDuLieu, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TapDuLieu> capNhatTapDuLieu(
            @PathVariable("id") Long id,
            @RequestBody TapDuLieu tapDuLieu) {
        Optional<TapDuLieu> tapDuLieuData = tapDuLieuService.layTapDuLieuTheoId(id);

        if (tapDuLieuData.isPresent()) {
            TapDuLieu _tapDuLieu = tapDuLieuData.get();
            _tapDuLieu.setTen(tapDuLieu.getTen());
            _tapDuLieu.setMoTa(tapDuLieu.getMoTa());
            _tapDuLieu.setLoai(tapDuLieu.getLoai());

            return new ResponseEntity<>(tapDuLieuService.luuTapDuLieu(_tapDuLieu), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> xoaTapDuLieu(@PathVariable("id") Long id) {
        try {
            tapDuLieuService.xoaTapDuLieu(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
