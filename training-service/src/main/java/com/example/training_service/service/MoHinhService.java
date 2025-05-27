package com.example.training_service.service;

import com.example.training_service.model.LoaiMoHinh;
import com.example.training_service.model.MauBaoLuc;
import com.example.training_service.model.MoHinhDaHuanLuyen;
import com.example.training_service.model.TapDuLieu;
import com.example.training_service.util.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MoHinhService {

    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public MoHinhService(ApiClient apiClient, ObjectMapper objectMapper) {
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> layDanhSachMoHinh(String mucDich, String trangThai, int trang, int soLuong) {
        Map<String, Object> queryParams = new HashMap<>();
        if (mucDich != null && !mucDich.isEmpty()) {
            queryParams.put("mucDich", mucDich);
        }
        if (trangThai != null && !trangThai.isEmpty()) {
            queryParams.put("trangThai", trangThai);
        }
        queryParams.put("trang", trang);
        queryParams.put("soLuong", soLuong);

        ResponseEntity<Map<String, Object>> response = apiClient.getEntityFromDatabase(
                "/mo-hinh-da-huan-luyen",
                new ParameterizedTypeReference<Map<String, Object>>() {},
                queryParams
        );

        return apiClient.parsePaginatedResponse(response, "moHinhDaHuanLuyens", MoHinhDaHuanLuyen.class);
    }

    public MoHinhDaHuanLuyen layMoHinhTheoId(Long id) {
        return apiClient.getFromDatabase("/mo-hinh-da-huan-luyen/" + id, MoHinhDaHuanLuyen.class);
    }

    public List<LoaiMoHinh> layDanhSachLoaiMoHinh() {
        ResponseEntity<Map<String, Object>> response = apiClient.getEntityFromDatabase(
                "/loai-mo-hinh",
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> result = apiClient.parsePaginatedResponse(response, "loaiMoHinhs", LoaiMoHinh.class);
        return (List<LoaiMoHinh>) result.get("loaiMoHinhs");
    }

    public List<TapDuLieu> layDanhSachTapDuLieu() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("loai", "HUAN_LUYEN");

        ResponseEntity<Map<String, Object>> response = apiClient.getEntityFromDatabase(
                "/tap-du-lieu",
                new ParameterizedTypeReference<Map<String, Object>>() {},
                queryParams
        );

        Map<String, Object> result = apiClient.parsePaginatedResponse(response, "tapDuLieus", TapDuLieu.class);
        return (List<TapDuLieu>) result.get("tapDuLieus");
    }

    public List<MauBaoLuc> layTatCaMauBaoLuc() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("soLuong", 1000); // Get up to 1000 samples

        ResponseEntity<Map<String, Object>> response = apiClient.getEntityFromDatabase(
                "/mau-bao-luc",
                new ParameterizedTypeReference<Map<String, Object>>() {},
                queryParams
        );

        Map<String, Object> result = apiClient.parsePaginatedResponse(response, "mauBaoLucs", MauBaoLuc.class);
        return (List<MauBaoLuc>) result.get("mauBaoLucs");
    }

    public List<MauBaoLuc> layDanhSachMauTrongTapDuLieu(Long tapDuLieuId) {
        ResponseEntity<List<MauBaoLuc>> response = apiClient.getEntityFromDatabase(
                "/tap-du-lieu/" + tapDuLieuId + "/mau-bao-luc",
                new ParameterizedTypeReference<List<MauBaoLuc>>() {}
        );

        return response.getBody() != null ? response.getBody() : new ArrayList<>();
    }

    public void capNhatMauTrongTapDuLieu(Long tapDuLieuId, List<Long> mauIds) {
        // First, get current samples in the dataset
        List<MauBaoLuc> danhSachMauHienTai = layDanhSachMauTrongTapDuLieu(tapDuLieuId);
        List<Long> mauHienTaiIds = danhSachMauHienTai.stream()
                .map(MauBaoLuc::getId)
                .collect(Collectors.toList());

        for (MauBaoLuc mau : danhSachMauHienTai) {
            if (!mauIds.contains(mau.getId())) {
                apiClient.delete("/tap-du-lieu-mau/tap-du-lieu/" + tapDuLieuId + "/mau-bao-luc/" + mau.getId());
            }
        }

        for (Long mauId : mauIds) {
            if (!mauHienTaiIds.contains(mauId)) {
                apiClient.postToDatabase(
                        "/tap-du-lieu-mau/tap-du-lieu/" + tapDuLieuId + "/mau-bao-luc/" + mauId,
                        null,
                        Object.class
                );
            }
        }
    }

    public MoHinhDaHuanLuyen taoMoHinh(MoHinhDaHuanLuyen moHinh) throws Exception {
        // Thiết lập trạng thái ban đầu
        moHinh.setTrangThai(0);

        // Gọi API tạo mô hình
        Map<String, Object> queryParams = new HashMap<>();
        //queryParams.put("nguoiDungId", moHinh.getNguoiDungId());

        return apiClient.postToDatabase("/mo-hinh-da-huan-luyen", moHinh, MoHinhDaHuanLuyen.class, queryParams);
    }

    public MoHinhDaHuanLuyen capNhatMoHinh(Long id, MoHinhDaHuanLuyen moHinh) throws Exception {
        // Chuyển đổi thông số huấn luyện từ form thành JSON
        // if (moHinh.getThongSoForm() != null) {
        //     String thongSoJson = objectMapper.writeValueAsString(moHinh.getThongSoForm());
        //     moHinh.setThongSoHuanLuyen(thongSoJson);
        // }

        // Gọi API cập nhật mô hình
        apiClient.putToDatabase("/mo-hinh-da-huan-luyen/" + id, moHinh);

        // Lấy thông tin mô hình sau khi cập nhật
        return layMoHinhTheoId(id);
    }

    public MoHinhDaHuanLuyen capNhatTrangThai(Long id, String trangThai) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("trangThai", trangThai);

        apiClient.putToDatabase("/mo-hinh-da-huan-luyen/" + id + "/cap-nhat-trang-thai", null, queryParams);

        // Lấy thông tin mô hình sau khi cập nhật
        return layMoHinhTheoId(id);
    }

    public void xoaMoHinh(Long id) {
        try {
            // Lấy thông tin mẫu để xóa file
            MoHinhDaHuanLuyen mau = layMoHinhTheoId(id);

            // Xóa thông tin trong database
            apiClient.delete("/mo-hinh-da-huan-luyen/" + id);
            System.out.println("thanh cong");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa mẫu bạo lực: " + e.getMessage(), e);
        }
    }
}