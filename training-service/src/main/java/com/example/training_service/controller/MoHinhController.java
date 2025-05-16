package com.example.training_service.controller;

import com.example.training_service.model.LoaiMoHinh;
import com.example.training_service.model.MoHinhDaHuanLuyen;
import com.example.training_service.model.TapDuLieu;
import com.example.training_service.service.HuanLuyenService;
import com.example.training_service.service.MoHinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mo-hinh")
public class MoHinhController {

    private final MoHinhService moHinhService;
    private final HuanLuyenService huanLuyenService;

    @Autowired
    public MoHinhController(MoHinhService moHinhService, HuanLuyenService huanLuyenService) {
        this.moHinhService = moHinhService;
        this.huanLuyenService = huanLuyenService;
    }

    // ===== GET: Danh sách mô hình =====
    @GetMapping
    public String danhSach(
            @RequestParam(required = false) String mucDich,
            @RequestParam(required = false) String trangThai,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int soLuong,
            Model model) {

        Map<String, Object> result = moHinhService.layDanhSachMoHinh(mucDich, trangThai, trang, soLuong);

        model.addAttribute("moHinhs", result.get("moHinhDaHuanLuyens"));
        model.addAttribute("trangHienTai", result.get("trangHienTai"));
        model.addAttribute("tongSoMuc", result.get("tongSoMuc"));
        model.addAttribute("tongSoTrang", result.get("tongSoTrang"));
        model.addAttribute("mucDich", mucDich);
        model.addAttribute("trangThai", trangThai);

        return "mo-hinh/list";
    }

    // ===== GET: Chi tiết mô hình =====
    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        MoHinhDaHuanLuyen moHinh = moHinhService.layMoHinhTheoId(id);
        model.addAttribute("moHinh", moHinh);
        return "mo-hinh/detail";
    }

    // ===== GET: Form thêm mới mô hình =====
    @GetMapping("/them-moi")
    public String formThemMoi(Model model) {
        List<LoaiMoHinh> loaiMoHinhs = moHinhService.layDanhSachLoaiMoHinh();
        List<TapDuLieu> tapDuLieus = moHinhService.layDanhSachTapDuLieu();

        model.addAttribute("moHinh", new MoHinhDaHuanLuyen());
        model.addAttribute("loaiMoHinhs", loaiMoHinhs);
        model.addAttribute("tapDuLieus", tapDuLieus);
        model.addAttribute("isNew", true);

        return "mo-hinh/train";
    }

    // ===== POST: Xử lý thêm mới mô hình =====
    @PostMapping("/them-moi")
    public String themMoi(
            @ModelAttribute MoHinhDaHuanLuyen moHinh,
            @RequestParam(defaultValue = "1") Long nguoiDungId,
            RedirectAttributes redirectAttributes) {

        try {
            MoHinhDaHuanLuyen moHinhMoi = moHinhService.taoMoHinh(moHinh);
            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo mô hình thành công!");
            return "redirect:/mo-hinh/" + moHinhMoi.getId();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xử lý: " + e.getMessage());
            return "redirect:/mo-hinh/them-moi";
        }
    }

    // ===== GET: Form huấn luyện mô hình =====
    @GetMapping("/huan-luyen/{id}")
    public String formHuanLuyen(@PathVariable Long id, Model model) {
        MoHinhDaHuanLuyen moHinh = moHinhService.layMoHinhTheoId(id);

        model.addAttribute("moHinh", moHinh);
        model.addAttribute("isNew", false);

        return "mo-hinh/train";
    }

    // ===== POST: Xử lý huấn luyện mô hình =====
    @PostMapping("/huan-luyen/{id}")
    public String huanLuyen(
            @PathVariable Long id,
            @ModelAttribute MoHinhDaHuanLuyen moHinh,
            RedirectAttributes redirectAttributes) {

        try {
            System.out.println("Starting training for model ID: " + id);
            boolean ketQua = huanLuyenService.batDauHuanLuyen(id);
            System.out.println("Training result: " + ketQua);

            if (ketQua) {
                redirectAttributes.addFlashAttribute("successMessage", "Đã bắt đầu huấn luyện mô hình thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi bắt đầu huấn luyện mô hình.");
            }

            return "redirect:/mo-hinh/" + id;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xử lý: " + e.getMessage());
            return "redirect:/mo-hinh/huan-luyen/" + id;
        }
    }

    // ===== GET: Xoá mô hình =====
    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id) {
        try {
            moHinhService.xoaMoHinh(id);
            return "redirect:/mo-hinh";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
}
