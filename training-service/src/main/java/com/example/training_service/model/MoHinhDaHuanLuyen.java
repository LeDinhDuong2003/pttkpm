// training-service/src/main/java/com/example/training_service/model/MoHinhDaHuanLuyen.java
package com.example.training_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    // THÊM @JsonFormat để serialize thành string thay vì array
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime thoiGianBatDau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime thoiGianKetThuc;

    private Integer trangThai; // DANG_HUAN_LUYEN, HOAN_THANH, LOI

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayTao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayCapNhat;
}