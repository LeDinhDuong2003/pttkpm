package com.example.mau_service.controller;

import com.example.mau_service.model.MauBaoLuc;
import com.example.mau_service.model.TapDuLieu;
import com.example.mau_service.service.MauBaoLucService;
import com.example.mau_service.service.TapDuLieuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tap-du-lieu")
public class TapDuLieuController {

    private final TapDuLieuService tapDuLieuService;
    private final MauBaoLucService mauBaoLucService;

    public TapDuLieuController(TapDuLieuService tapDuLieuService, MauBaoLucService mauBaoLucService) {
        this.tapDuLieuService = tapDuLieuService;
        this.mauBaoLucService = mauBaoLucService;
    }

    @GetMapping
    public String danhSach(
            @RequestParam(required = false) String loai,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int soLuong,
            Model model) {

        Map<String, Object> result = tapDuLieuService.layDanhSachTapDuLieu(loai, trang, soLuong);

        // Lấy danh sách dưới dạng List<Object> (thực tế là List<LinkedHashMap>)
        List<Object> tapDuLieusList = (List<Object>) result.get("tapDuLieus");
        List<TapDuLieu> tapDuLieus = new ArrayList<>();

        // Chuyển đổi từng LinkedHashMap thành đối tượng TapDuLieu
        if (tapDuLieusList != null) {
            ObjectMapper mapper = new ObjectMapper();
            for (Object item : tapDuLieusList) {
                TapDuLieu tapDuLieu = mapper.convertValue(item, TapDuLieu.class);

                // Lấy danh sách mẫu cho mỗi tập dữ liệu
                List<MauBaoLuc> danhSachMau = tapDuLieuService.layDanhSachMauBaoLucTrongTapDuLieu(tapDuLieu.getId());
                tapDuLieu.setDanhSachMau(danhSachMau);

                tapDuLieus.add(tapDuLieu);
            }
        }

        model.addAttribute("tapDuLieus", tapDuLieus);
        model.addAttribute("trangHienTai", result.get("trangHienTai"));
        model.addAttribute("tongSoMuc", result.get("tongSoMuc"));
        model.addAttribute("tongSoTrang", result.get("tongSoTrang"));
        model.addAttribute("loai", loai);

        return "tap-du-lieu/list";
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        TapDuLieu tapDuLieu = tapDuLieuService.layTapDuLieuTheoId(id);
        List<MauBaoLuc> danhSachMau = tapDuLieu.getDanhSachMau();

        model.addAttribute("tapDuLieu", tapDuLieu);
        model.addAttribute("danhSachMau", danhSachMau);

        return "tap-du-lieu/detail";
    }

    @GetMapping("/them-moi")
    public String formThemMoi(Model model) {
        // Lấy tất cả mẫu bạo lực để hiển thị trong form
        Map<String, Object> result = mauBaoLucService.layDanhSachMauBaoLuc(null, 0, 1000);
        List<MauBaoLuc> danhSachMau = (List<MauBaoLuc>) result.get("mauBaoLucs");
        System.out.println(danhSachMau.size());

        TapDuLieu tapDuLieu = new TapDuLieu();

        model.addAttribute("tapDuLieu", tapDuLieu);
        model.addAttribute("danhSachMau", danhSachMau);
        model.addAttribute("isNew", true);

        return "tap-du-lieu/add";
    }

    @PostMapping("/them-moi")
    public String themMoi(@ModelAttribute TapDuLieu tapDuLieu, @RequestParam(name = "mauIds", required = false) List<Long> mauIds) {
        try {
            System.out.println("TapDuLieu: " + tapDuLieu.getTen());
            System.out.println("MauIds: " + mauIds);

            tapDuLieuService.taoTapDuLieu(tapDuLieu, mauIds);
            return "redirect:/tap-du-lieu";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/chinh-sua/{id}")
    public String formChinhSua(@PathVariable Long id, Model model) {
        // Lấy tất cả mẫu bạo lực để hiển thị trong form
        Map<String, Object> result = mauBaoLucService.layDanhSachMauBaoLuc(null, 0, 1000);
        List<MauBaoLuc> danhSachMau = (List<MauBaoLuc>) result.get("mauBaoLucs");

        TapDuLieu tapDuLieu = tapDuLieuService.layTapDuLieuTheoId(id);

        model.addAttribute("tapDuLieu", tapDuLieu);
        model.addAttribute("danhSachMau", danhSachMau);
        model.addAttribute("isNew", false);

        return "tap-du-lieu/edit";
    }

    @PostMapping("/chinh-sua/{id}")
    public String chinhSua(@PathVariable Long id, @ModelAttribute TapDuLieu tapDuLieu,
                           @RequestParam(name = "mauIds", required = false) List<Long> mauIds) {
        try {
            tapDuLieuService.capNhatTapDuLieu(id, tapDuLieu, mauIds);
            return "redirect:/tap-du-lieu/" + id;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id) {
        try {
            tapDuLieuService.xoaTapDuLieu(id);
            return "redirect:/tap-du-lieu";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}