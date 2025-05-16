package com.example.mau_service.service;

import com.example.mau_service.model.MauBaoLuc;
import com.example.mau_service.util.ApiClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MauBaoLucService {

    private static final Logger logger = LoggerFactory.getLogger(MauBaoLucService.class);
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

    public MauBaoLuc taoMauBaoLuc(MauBaoLuc mauBaoLuc,MultipartFile videoFile) {
        try {
            if (videoFile != null && !videoFile.isEmpty()) {
                logger.info("Đang xử lý file video: {} ({} bytes)",
                        videoFile.getOriginalFilename(), videoFile.getSize());

                String filePath = fileStorageService.storeFile(videoFile);
                if (filePath != null) {
                    String relativePath = fileStorageService.getRelativeFilePath(filePath);
                    mauBaoLuc.setDuongDanVideo(relativePath);
                    logger.info("Đã lưu file video với đường dẫn: {}", relativePath);
                } else {
                    logger.warn("Không thể lưu file video");
                }
            } else {
                logger.info("Không có file video để xử lý");
            }

            // Gửi dữ liệu đến database service
            Map<String, Object> queryParams = new HashMap<>();
            MauBaoLuc result = apiClient.post("/mau-bao-luc", mauBaoLuc, MauBaoLuc.class, queryParams);
            logger.info("Đã tạo mẫu bạo lực trong database với ID: {}", result.getId());

            return result;
        } catch (Exception e) {
            logger.error("Lỗi khi tạo mẫu bạo lực: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi tạo mẫu bạo lực: " + e.getMessage(), e);
        }
    }

    public void capNhatMauBaoLuc(Long id, MauBaoLuc mauBaoLuc,MultipartFile videoFile) {
        try {
            MauBaoLuc mauHienTai = layMauBaoLucTheoId(id);
            logger.info("Cập nhật mẫu bạo lực ID: {}", id);

            if (videoFile != null && !videoFile.isEmpty()) {
                logger.info("Đang xử lý file video mới: {} ({} bytes)",
                        videoFile.getOriginalFilename(), videoFile.getSize());

                // Xóa file cũ nếu có
                if (mauHienTai.getDuongDanVideo() != null && !mauHienTai.getDuongDanVideo().isEmpty()) {
                    boolean deleted = fileStorageService.deleteFile(mauHienTai.getDuongDanVideo());
                    if (deleted) {
                        logger.info("Đã xóa file video cũ: {}", mauHienTai.getDuongDanVideo());
                    } else {
                        logger.warn("Không thể xóa file video cũ: {}", mauHienTai.getDuongDanVideo());
                    }
                }

                // Upload
                String filePath = fileStorageService.storeFile(videoFile);
                if (filePath != null) {
                    String relativePath = fileStorageService.getRelativeFilePath(filePath);
                    mauBaoLuc.setDuongDanVideo(relativePath);
                    logger.info("Đã lưu file video mới với đường dẫn: {}", relativePath);
                } else {
                    logger.warn("Không thể lưu file video mới");
                }
            } else {
                mauBaoLuc.setDuongDanVideo(mauHienTai.getDuongDanVideo());
                logger.info("Giữ nguyên file video cũ: {}", mauHienTai.getDuongDanVideo());
            }

            // Cập nhật thông tin trong database
            apiClient.put("/mau-bao-luc/" + id, mauBaoLuc);
            logger.info("Đã cập nhật thông tin mẫu bạo lực trong database");
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật mẫu bạo lực: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi cập nhật mẫu bạo lực: " + e.getMessage(), e);
        }
    }

    public void xoaMauBaoLuc(Long id) {
        try {
            // Lấy thông tin mẫu để xóa file
            MauBaoLuc mau = layMauBaoLucTheoId(id);
            logger.info("Xóa mẫu bạo lực ID: {}", id);

            if (mau != null && mau.getDuongDanVideo() != null && !mau.getDuongDanVideo().isEmpty()) {
                // Xóa file
                boolean deleted = fileStorageService.deleteFile(mau.getDuongDanVideo());
                if (deleted) {
                    logger.info("Đã xóa file video: {}", mau.getDuongDanVideo());
                } else {
                    logger.warn("Không thể xóa file video: {}", mau.getDuongDanVideo());
                }
            }

            // Xóa thông tin trong database
            apiClient.delete("/mau-bao-luc/" + id);
            logger.info("Đã xóa mẫu bạo lực khỏi database");
        } catch (Exception e) {
            logger.error("Lỗi khi xóa mẫu bạo lực: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi xóa mẫu bạo lực: " + e.getMessage(), e);
        }
    }
}