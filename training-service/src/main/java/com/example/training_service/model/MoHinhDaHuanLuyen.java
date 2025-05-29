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
    private String mucDich;
    private String duongDanMoHinh;
    private String thongSoHuanLuyen;
    private TapDuLieu tapDuLieu;
    private String phienBan;
    private Float doChinhXac;

    // Sử dụng @JsonFormat để serialize/deserialize thành string với timezone
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime thoiGianBatDau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime thoiGianKetThuc;

    private Integer trangThai;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime ngayTao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime ngayCapNhat;
}