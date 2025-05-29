// training-service/src/main/java/com/example/training_service/dto/TrainingRequest.java
package com.example.training_service.dto;

import com.example.training_service.model.MauBaoLuc;
import com.example.training_service.model.MoHinhDaHuanLuyen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TrainingRequest {
    private MoHinhDaHuanLuyen moHinh;
    private List<MauBaoLuc> danhSachMau;

    // Constructor với các tham số chính
    public TrainingRequest(MoHinhDaHuanLuyen moHinh, List<MauBaoLuc> danhSachMau) {
        this.moHinh = moHinh;
        this.danhSachMau = danhSachMau;
    }
}