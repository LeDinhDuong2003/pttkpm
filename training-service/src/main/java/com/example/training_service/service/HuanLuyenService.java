// training-service/src/main/java/com/example/training_service/service/HuanLuyenService.java
package com.example.training_service.service;

import com.example.training_service.model.MoHinhDaHuanLuyen;
import com.example.training_service.util.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public boolean batDauHuanLuyen(Long moHinhId) {
        try {
            // Gửi yêu cầu đến training processor
            ResponseEntity<Map<String, Object>> response = apiClient.requestTraining(moHinhId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Boolean success = (Boolean) responseBody.get("success");

                if (success != null && success) {
                    // Cập nhật trạng thái mô hình thành "DANG_HUAN_LUYEN" (status = 1)
                    moHinhService.capNhatTrangThai(moHinhId, "1");

                    // Debug log
                    String message = (String) responseBody.get("message");
                    Integer queuePosition = (Integer) responseBody.get("queue_position");
                    System.out.println("Success: " + message);
                    System.out.println("Queue position: " + queuePosition);

                    return true;
                } else {
                    // Nếu success là false hoặc null
                    String message = (String) responseBody.get("message");
                    System.out.println("Failed to start training: " + message);

                    // Cập nhật trạng thái mô hình thành "LOI"
                    moHinhService.capNhatTrangThai(moHinhId, "4");
                    return false;
                }
            } else {
                // Nếu có lỗi HTTP
                System.out.println("HTTP Error: " + response.getStatusCode());

                // Cập nhật trạng thái mô hình thành "LOI"
                moHinhService.capNhatTrangThai(moHinhId, "4");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            // Nếu có lỗi, cập nhật trạng thái mô hình thành "LOI"
            moHinhService.capNhatTrangThai(moHinhId, "4");

            return false;
        }
    }
}