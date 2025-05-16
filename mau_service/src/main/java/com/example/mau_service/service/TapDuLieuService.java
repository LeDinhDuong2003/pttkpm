package com.example.mau_service.service;

import com.example.mau_service.model.MauBaoLuc;
import com.example.mau_service.model.TapDuLieu;
import com.example.mau_service.util.ApiClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TapDuLieuService {

    private final ApiClient apiClient;
    private final MauBaoLucService mauBaoLucService;

    public TapDuLieuService(ApiClient apiClient, MauBaoLucService mauBaoLucService) {
        this.apiClient = apiClient;
        this.mauBaoLucService = mauBaoLucService;
    }

    public Map<String, Object> layDanhSachTapDuLieu(String loai, int trang, int soLuong) {
        Map<String, Object> queryParams = new HashMap<>();
        if (loai != null && !loai.isEmpty()) {
            queryParams.put("loai", loai);
        }
        queryParams.put("trang", trang);
        queryParams.put("soLuong", soLuong);

        ResponseEntity<Map<String, Object>> response = apiClient.getForEntity(
                "/tap-du-lieu",
                new ParameterizedTypeReference<Map<String, Object>>() {},
                queryParams
        );

        return apiClient.parsePaginatedResponse(response, "tapDuLieus", TapDuLieu.class);
    }

    public TapDuLieu layTapDuLieuTheoId(Long id) {
        TapDuLieu tapDuLieu = apiClient.get("/tap-du-lieu/" + id, TapDuLieu.class);

        // Lấy danh sách mẫu trong tập dữ liệu
        List<MauBaoLuc> danhSachMau = layDanhSachMauBaoLucTrongTapDuLieu(id);
        tapDuLieu.setDanhSachMau(danhSachMau);

        return tapDuLieu;
    }

    public List<MauBaoLuc> layDanhSachMauBaoLucTrongTapDuLieu(Long tapDuLieuId) {
        ResponseEntity<List<MauBaoLuc>> response = apiClient.getForEntity(
                "/tap-du-lieu/" + tapDuLieuId + "/mau-bao-luc",
                new ParameterizedTypeReference<List<MauBaoLuc>>() {}
        );

        return response.getBody() != null ? response.getBody() : new ArrayList<>();
    }

    public TapDuLieu taoTapDuLieu(TapDuLieu tapDuLieu, List<Long> mauIds) {
        // Tạo tập dữ liệu đầu tiên
        Map<String, Object> queryParams = new HashMap<>();
        TapDuLieu tapDuLieuSaved = apiClient.post("/tap-du-lieu", tapDuLieu, TapDuLieu.class, queryParams);

        if (mauIds != null && !mauIds.isEmpty()) {
            for (Long mauId : mauIds) {
                themMauVaoTapDuLieu(tapDuLieuSaved.getId(), mauId);
            }
        }

        return tapDuLieuSaved;
    }

    public void capNhatTapDuLieu(Long id, TapDuLieu tapDuLieu, List<Long> mauIds) {
        // Cập nhật thông tin tập dữ liệu
        apiClient.put("/tap-du-lieu/" + id, tapDuLieu);

        // Lấy danh sách mẫu hiện tại trong tập dữ liệu
        List<MauBaoLuc> danhSachMauHienTai = layDanhSachMauBaoLucTrongTapDuLieu(id);

        // Nếu không có mauIds thì mặc định là không chọn mẫu nào
        List<Long> selectedMauIds = mauIds != null ? mauIds : new ArrayList<>();

        // Xóa các mẫu không còn được chọn
        for (MauBaoLuc mau : danhSachMauHienTai) {
            if (!selectedMauIds.contains(mau.getId())) {
                xoaMauKhoiTapDuLieu(id, mau.getId());
            }
        }

        // Thêm các mẫu mới được chọn
        for (Long mauId : selectedMauIds) {
            boolean daTonTai = false;
            for (MauBaoLuc mau : danhSachMauHienTai) {
                if (mau.getId().equals(mauId)) {
                    daTonTai = true;
                    break;
                }
            }

            if (!daTonTai) {
                themMauVaoTapDuLieu(id, mauId);
            }
        }
    }

    public void xoaTapDuLieu(Long id) {
        apiClient.delete("/tap-du-lieu/" + id);
    }

    public void themMauVaoTapDuLieu(Long tapDuLieuId, Long mauBaoLucId) {
        apiClient.post("/tap-du-lieu-mau/tap-du-lieu/" + tapDuLieuId + "/mau-bao-luc/" + mauBaoLucId, null, Void.class);
    }

    public void xoaMauKhoiTapDuLieu(Long tapDuLieuId, Long mauBaoLucId) {
        apiClient.delete("/tap-du-lieu-mau/tap-du-lieu/" + tapDuLieuId + "/mau-bao-luc/" + mauBaoLucId);
    }
}