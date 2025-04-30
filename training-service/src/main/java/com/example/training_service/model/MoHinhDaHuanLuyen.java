package com.example.training_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoHinhDaHuanLuyen {
    private Long id;
    private String ten;
    private String moTa;
    private LoaiMoHinh loaiMoHinh;
    private String mucDich; // PHAT_HIEN_BAO_LUC, NHAN_DANG_VUNG_CHUA, NHAN_DANG_CHUYEN_DONG
    private String duongDanMoHinh;
    private String thongSoHuanLuyen; // JSON chứa các thông số huấn luyện
    private TapDuLieu tapDuLieu;
    private String phienBan;
    private Float doChinhXac;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private Integer trangThai; // DANG_HUAN_LUYEN, HOAN_THANH, LOI
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

}