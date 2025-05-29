// database_service/src/main/java/com/example/database_service/model/MoHinhDaHuanLuyen.java
package com.example.database_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mo_hinh_da_huan_luyen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoHinhDaHuanLuyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ten;

    @Column(columnDefinition = "TEXT")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "loai_mo_hinh_id")
    private LoaiMoHinh loaiMoHinh;

    // Bỏ @Enumerated vì bạn dùng String
    private String mucDich;

    private String duongDanMoHinh;

    @Column(columnDefinition = "TEXT")
    private String thongSoHuanLuyen;

    @ManyToOne
    @JoinColumn(name = "tap_du_lieu_id")
    private TapDuLieu tapDuLieu;

    private String phienBan;

    private Float doChinhXac;

    // Sử dụng @JsonFormat để serialize/deserialize LocalDateTime thành string
    @Column(columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime thoiGianBatDau;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime thoiGianKetThuc;

    // Bỏ @Enumerated vì bạn dùng Integer
    private Integer trangThai;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime ngayTao;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}