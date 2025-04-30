package com.example.mau_service.service;

import com.example.mau_service.model.MauBaoLuc;
import com.example.mau_service.util.ApiClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MauBaoLucService {

    private final ApiClient apiClient;
    private final FileStorageService fileStorageService;

    public MauBaoLucService(ApiClient apiClient, FileStorageService fileStorageService) {
        this.apiClient = apiClient;
        this.fileStorageService = fileStorageService;
    }

    public Map<String, Object> layDanhSachMauBaoLuc(String nhan, int trang, int soLuong) {
        Map<String, Object> queryParams = new HashMap<>();
        if (nhan != null && !nhan.isEmpty()) {
            queryParams.put("nhan", nhan);
        }
        queryParams.put("trang", trang);
        queryParams.put("soLuong", soLuong);

        ResponseEntity<Map<String, Object>> response = apiClient.getForEntity(
                "/mau-bao-luc",
                new ParameterizedTypeReference<Map<String, Object>>() {},
                queryParams
        );

        return apiClient.parsePaginatedResponse(response, "mauBaoLucs", MauBaoLuc.class);
    }

    public MauBaoLuc layMauBaoLucTheoId(Long id) {
        return apiClient.get("/mau-bao-luc/" + id, MauBaoLuc.class);
    }

    public List<MauBaoLuc> layMauBaoLucTheoNhan(String nhan) {
        ResponseEntity<List<MauBaoLuc>> response = apiClient.getForEntity(
                "/mau-bao-luc/nhan/" + nhan,
                new ParameterizedTypeReference<List<MauBaoLuc>>() {}
        );

        return response.getBody() != null ? response.getBody() : new ArrayList<>();
    }

    public MauBaoLuc taoMauBaoLuc(MauBaoLuc mauBaoLuc) {
        // Không xử lý videoFile ở đây nữa vì đã xử lý ở Controller
        Map<String, Object> queryParams = new HashMap<>();

        return apiClient.post("/mau-bao-luc", mauBaoLuc, MauBaoLuc.class, queryParams);
    }

    public void capNhatMauBaoLuc(Long id, MauBaoLuc mauBaoLuc) {
        // Lấy thông tin mẫu hiện tại
        MauBaoLuc mauHienTai = layMauBaoLucTheoId(id);

        // Xử lý upload file nếu có
        MultipartFile videoFile = mauBaoLuc.getVideoFile();
        if (videoFile != null && !videoFile.isEmpty()) {
            // Xóa file cũ nếu có
            if (mauHienTai.getDuongDanVideo() != null) {
                String oldFileName = Paths.get(mauHienTai.getDuongDanVideo()).getFileName().toString();
                fileStorageService.deleteFile(oldFileName);
            }

            // Upload file mới
            String fileName = fileStorageService.storeFile(videoFile);
            mauBaoLuc.setDuongDanVideo(fileStorageService.getRelativeFilePath(fileName));
        } else {
            // Giữ nguyên đường dẫn video cũ
            mauBaoLuc.setDuongDanVideo(mauHienTai.getDuongDanVideo());
        }

        apiClient.put("/mau-bao-luc/" + id, mauBaoLuc);
    }

    public void xoaMauBaoLuc(Long id) {
        // Lấy thông tin mẫu để xóa file
        MauBaoLuc mau = layMauBaoLucTheoId(id);
        if (mau != null && mau.getDuongDanVideo() != null) {
            String fileName = Paths.get(mau.getDuongDanVideo()).getFileName().toString();
            fileStorageService.deleteFile(fileName);
        }

        apiClient.delete("/mau-bao-luc/" + id);
    }
}