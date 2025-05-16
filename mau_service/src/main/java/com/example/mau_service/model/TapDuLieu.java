package com.example.mau_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TapDuLieu {
    private Long id;
    private String ten;
    private String moTa;
    private String loai; // HUAN_LUYEN, KIEM_TRA, DANH_GIA

    private List<MauBaoLuc> danhSachMau;

}