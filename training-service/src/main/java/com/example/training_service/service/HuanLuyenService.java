package com.example.training_service.service;

import com.example.training_service.model.MoHinhDaHuanLuyen;
import com.example.training_service.util.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class HuanLuyenService {

    private final ApiClient apiClient;
    private final MoHinhService moHinhService;

    @Autowired
    public HuanLuyenService(ApiClient apiClient, MoHinhService moHinhService) {
        this.apiClient = apiClient;
        this.moHinhService = moHinhService;
    }

    public boolean batDauHuanLuyen(Long moHinhId) {
        try {
            // Cập nhật trạng thái mô hình thành "DANG_HUAN_LUYEN"
            MoHinhDaHuanLuyen moHinh = moHinhService.capNhatTrangThai(moHinhId, "DANG_HUAN_LUYEN");

            // Gửi yêu cầu đến training processor
            ResponseEntity<String> response = apiClient.requestTraining(moHinhId);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

            // Nếu có lỗi, cập nhật trạng thái mô hình thành "LOI"
            moHinhService.capNhatTrangThai(moHinhId, "LOI");

            return false;
        }
    }
}