package com.example.database_service.controller;


import com.example.database_service.model.MoHinhDaHuanLuyen;
import com.example.database_service.service.MoHinhDaHuanLuyenService;
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
@RequestMapping("/api/mo-hinh-da-huan-luyen")
public class MoHinhDaHuanLuyenController {

    @Autowired
    private MoHinhDaHuanLuyenService moHinhDaHuanLuyenService;


    @GetMapping
    public ResponseEntity<Map<String, Object>> layDanhSachMoHinhDaHuanLuyen(
            @RequestParam(required = false) String mucDich,
            @RequestParam(required = false) String trangThai,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int soLuong) {

        try {
            System.out.println("mucDich: " + mucDich);
            System.out.println("trangThai: " + trangThai);
            String sapXepTheo = "id";
            String huongSapXep = "asc";
            Sort.Direction huong = "desc".equalsIgnoreCase(huongSapXep) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable paging = PageRequest.of(trang, soLuong, Sort.by(huong, sapXepTheo));

            Page<MoHinhDaHuanLuyen> pageMoHinhDaHuanLuyen;

            if (trangThai != null && !trangThai.isEmpty() && mucDich != null && !mucDich.isEmpty()) {
                try {
                    Integer trangThaiEnum = Integer.parseInt(trangThai);
                    pageMoHinhDaHuanLuyen = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoTrangThaiVaMucDich(mucDich,trangThaiEnum, paging);
                } catch (IllegalArgumentException e) {
                    pageMoHinhDaHuanLuyen = moHinhDaHuanLuyenService.layTatCaMoHinhDaHuanLuyen(paging);
                }
            } else if (mucDich != null && !mucDich.isEmpty()) {
                try {
                    String mucDichEnum = mucDich;
                    pageMoHinhDaHuanLuyen = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoMucDich(mucDichEnum, paging);
                } catch (IllegalArgumentException e) {
                    pageMoHinhDaHuanLuyen = moHinhDaHuanLuyenService.layTatCaMoHinhDaHuanLuyen(paging);
                }
            } else if (trangThai != null && !trangThai.isEmpty()) {
                try {
                    Integer trangThaiEnum = Integer.parseInt(trangThai);
                    pageMoHinhDaHuanLuyen = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoTrangThai(trangThaiEnum, paging);
                } catch (IllegalArgumentException e) {
                    pageMoHinhDaHuanLuyen = moHinhDaHuanLuyenService.layTatCaMoHinhDaHuanLuyen(paging);
                }
            } else {
                pageMoHinhDaHuanLuyen = moHinhDaHuanLuyenService.layTatCaMoHinhDaHuanLuyen(paging);
            }

            List<MoHinhDaHuanLuyen> moHinhDaHuanLuyens = pageMoHinhDaHuanLuyen.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("moHinhDaHuanLuyens", moHinhDaHuanLuyens);
            response.put("trangHienTai", pageMoHinhDaHuanLuyen.getNumber());
            response.put("tongSoMuc", pageMoHinhDaHuanLuyen.getTotalElements());
            response.put("tongSoTrang", pageMoHinhDaHuanLuyen.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tiếp tục MoHinhDaHuanLuyenController.java
    @GetMapping("/{id}")
    public ResponseEntity<MoHinhDaHuanLuyen> layMoHinhDaHuanLuyenTheoId(@PathVariable("id") Long id) {
        Optional<MoHinhDaHuanLuyen> moHinhDaHuanLuyenData = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoId(id);

        if (moHinhDaHuanLuyenData.isPresent()) {
            return new ResponseEntity<>(moHinhDaHuanLuyenData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/loai-mo-hinh/{loaiMoHinhId}")
    public ResponseEntity<List<MoHinhDaHuanLuyen>> layMoHinhDaHuanLuyenTheoLoaiMoHinh(@PathVariable("loaiMoHinhId") Long loaiMoHinhId) {
        try {
            List<MoHinhDaHuanLuyen> moHinhDaHuanLuyens = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoLoaiMoHinh(loaiMoHinhId);

            if (moHinhDaHuanLuyens.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(moHinhDaHuanLuyens, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/tap-du-lieu/{tapDuLieuId}")
//    public ResponseEntity<List<MoHinhDaHuanLuyen>> layMoHinhDaHuanLuyenTheoTapDuLieu(@PathVariable("tapDuLieuId") Long tapDuLieuId) {
//        try {
//            List<MoHinhDaHuanLuyen> moHinhDaHuanLuyens = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoTapDuLieu(tapDuLieuId);
//
//            if (moHinhDaHuanLuyens.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//            return new ResponseEntity<>(moHinhDaHuanLuyens, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @GetMapping("/muc-dich/{mucDich}")
//    public ResponseEntity<List<MoHinhDaHuanLuyen>> layMoHinhDaHuanLuyenTheoMucDich(@PathVariable("mucDich") String mucDich) {
//        try {
//            String mucDichEnum = mucDich;
//            List<MoHinhDaHuanLuyen> moHinhDaHuanLuyens = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoMucDich(mucDichEnum);
//
//            if (moHinhDaHuanLuyens.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//            return new ResponseEntity<>(moHinhDaHuanLuyens, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/trang-thai/{trangThai}")
    public ResponseEntity<List<MoHinhDaHuanLuyen>> layMoHinhDaHuanLuyenTheoTrangThai(@PathVariable("trangThai") String trangThai) {
        try {
            Integer trangThaiEnum = Integer.parseInt(trangThai);
            List<MoHinhDaHuanLuyen> moHinhDaHuanLuyens = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoTrangThai(trangThaiEnum);

            if (moHinhDaHuanLuyens.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(moHinhDaHuanLuyens, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<MoHinhDaHuanLuyen> taoMoHinhDaHuanLuyen(
            @RequestBody MoHinhDaHuanLuyen moHinhDaHuanLuyen) {
        try {
            MoHinhDaHuanLuyen _moHinhDaHuanLuyen = moHinhDaHuanLuyenService.luuMoHinhDaHuanLuyen(moHinhDaHuanLuyen);
            return new ResponseEntity<>(_moHinhDaHuanLuyen, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoHinhDaHuanLuyen> capNhatMoHinhDaHuanLuyen(
            @PathVariable("id") Long id,
            @RequestBody MoHinhDaHuanLuyen moHinhDaHuanLuyen) {
        Optional<MoHinhDaHuanLuyen> moHinhDaHuanLuyenData = moHinhDaHuanLuyenService.layMoHinhDaHuanLuyenTheoId(id);

        if (moHinhDaHuanLuyenData.isPresent()) {
            MoHinhDaHuanLuyen _moHinhDaHuanLuyen = moHinhDaHuanLuyenData.get();
            _moHinhDaHuanLuyen.setTen(moHinhDaHuanLuyen.getTen());
            _moHinhDaHuanLuyen.setMoTa(moHinhDaHuanLuyen.getMoTa());
            _moHinhDaHuanLuyen.setLoaiMoHinh(moHinhDaHuanLuyen.getLoaiMoHinh());
            _moHinhDaHuanLuyen.setMucDich(moHinhDaHuanLuyen.getMucDich());
            _moHinhDaHuanLuyen.setDuongDanMoHinh(moHinhDaHuanLuyen.getDuongDanMoHinh());
            _moHinhDaHuanLuyen.setThongSoHuanLuyen(moHinhDaHuanLuyen.getThongSoHuanLuyen());
            _moHinhDaHuanLuyen.setTapDuLieu(moHinhDaHuanLuyen.getTapDuLieu());
            _moHinhDaHuanLuyen.setPhienBan(moHinhDaHuanLuyen.getPhienBan());
            _moHinhDaHuanLuyen.setDoChinhXac(moHinhDaHuanLuyen.getDoChinhXac());

            return new ResponseEntity<>(moHinhDaHuanLuyenService.luuMoHinhDaHuanLuyen(_moHinhDaHuanLuyen), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/cap-nhat-trang-thai")
    public ResponseEntity<MoHinhDaHuanLuyen> capNhatTrangThaiMoHinhDaHuanLuyen(
            @PathVariable("id") Long id,
            @RequestParam String trangThai) {
        try {
            Integer trangThaiEnum = Integer.parseInt(trangThai);
            MoHinhDaHuanLuyen moHinhDaHuanLuyen = moHinhDaHuanLuyenService.capNhatTrangThai(id, trangThaiEnum);

            if (moHinhDaHuanLuyen == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(moHinhDaHuanLuyen, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> xoaMoHinhDaHuanLuyen(@PathVariable("id") Long id) {
        try {
            moHinhDaHuanLuyenService.xoaMoHinhDaHuanLuyen(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
