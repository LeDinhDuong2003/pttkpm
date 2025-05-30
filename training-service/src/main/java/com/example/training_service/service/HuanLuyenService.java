// training-service/src/main/java/com/example/training_service/service/HuanLuyenService.java
package com.example.training_service.service;

import com.example.training_service.model.MauBaoLuc;
import com.example.training_service.model.MoHinhDaHuanLuyen;
import com.example.training_service.util.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HuanLuyenService {

    private final ApiClient apiClient;
    private final MoHinhService moHinhService;

    @Autowired
    public HuanLuyenService(ApiClient apiClient, MoHinhService moHinhService) {
        this.apiClient = apiClient;
        this.moHinhService = moHinhService;
    }

    public boolean batDauHuanLuyen(MoHinhDaHuanLuyen moHinh, List<MauBaoLuc> danhSachMau) {
        try {
            Map<String, Object> requestPayload = new HashMap<>();
            requestPayload.put("moHinh", moHinh);
            requestPayload.put("danhSachMau", danhSachMau);
            requestPayload.put("moHinhId", moHinh.getId());

            ResponseEntity<Map<String, Object>> response = apiClient.requestTraining(requestPayload);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Boolean success = (Boolean) responseBody.get("success");

                if (success != null && success) {
                    moHinhService.capNhatTrangThai(moHinh.getId(),"1");

                    String message = (String) responseBody.get("message");
                    Integer queuePosition = (Integer) responseBody.get("queue_position");
                    System.out.println("Success: " + message);
                    System.out.println("Queue position: " + queuePosition);
                    System.out.println("Đã gửi " + danhSachMau.size() + " mẫu để huấn luyện");

                    return true;
                } else {
                    String message = (String) responseBody.get("message");
                    System.out.println("Failed to start training: " + message);

                    moHinhService.capNhatTrangThai(moHinh.getId(), "4");
                    return false;
                }
            } else {
                System.out.println("HTTP Error: " + response.getStatusCode());

                moHinhService.capNhatTrangThai(moHinh.getId(), "4");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            moHinhService.capNhatTrangThai(moHinh.getId(), "4");
            return false;
        }
    }

//    @Deprecated
//    public boolean batDauHuanLuyen(Long moHinhId) {
//        MoHinhDaHuanLuyen moHinh = moHinhService.layMoHinhTheoId(moHinhId);
//        if (moHinh == null || moHinh.getTapDuLieu() == null) {
//            return false;
//        }
//
//        List<MauBaoLuc> danhSachMau = moHinhService.layDanhSachMauTrongTapDuLieu(moHinh.getTapDuLieu().getId());
//        return batDauHuanLuyen(moHinh, danhSachMau);
//    }
}