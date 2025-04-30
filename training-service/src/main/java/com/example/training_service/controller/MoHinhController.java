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

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        MoHinhDaHuanLuyen moHinh = moHinhService.layMoHinhTheoId(id);
        model.addAttribute("moHinh", moHinh);

        return "mo-hinh/detail";
    }

    @GetMapping("/huan-luyen/{id}")
    public String formHuanLuyen(@PathVariable Long id, Model model) {
        MoHinhDaHuanLuyen moHinh = moHinhService.layMoHinhTheoId(id);

        // Nếu mô hình chưa có thông số huấn luyện, khởi tạo form thông số mặc định
//        if (moHinh.getThongSoForm() == null) {
//            moHinh.setThongSoForm(new MoHinhDaHuanLuyen.ThongSoForm());
//        }

        model.addAttribute("moHinh", moHinh);

        return "mo-hinh/train";
    }

    @PostMapping("/huan-luyen/{id}")
    public String huanLuyen(
            @PathVariable Long id,
            @ModelAttribute MoHinhDaHuanLuyen moHinh,
            RedirectAttributes redirectAttributes) {

        try {
            // Cập nhật thông số huấn luyện cho mô hình
            moHinhService.capNhatMoHinh(id, moHinh);

            // Bắt đầu quá trình huấn luyện
            boolean ketQua = huanLuyenService.batDauHuanLuyen(id);

            if (ketQua) {
                redirectAttributes.addFlashAttribute("successMessage", "Đã bắt đầu huấn luyện mô hình thành công!");
                // Tiếp tục MoHinhController.java
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

    @GetMapping("/them-moi")
    public String formThemMoi(Model model) {
        // Lấy danh sách loại mô hình
        List<LoaiMoHinh> loaiMoHinhs = moHinhService.layDanhSachLoaiMoHinh();

        // Lấy danh sách tập dữ liệu huấn luyện
        List<TapDuLieu> tapDuLieus = moHinhService.layDanhSachTapDuLieu();

        MoHinhDaHuanLuyen moHinh = new MoHinhDaHuanLuyen();
        // Khởi tạo form thông số mặc định
//        moHinh.setThongSoForm(new MoHinhDaHuanLuyen.ThongSoForm());

        model.addAttribute("moHinh", moHinh);
        model.addAttribute("loaiMoHinhs", loaiMoHinhs);
        model.addAttribute("tapDuLieus", tapDuLieus);
        model.addAttribute("isNew", true);

        return "mo-hinh/train";
    }

    @PostMapping("/them-moi")
    public String themMoi(
            @ModelAttribute MoHinhDaHuanLuyen moHinh,
            @RequestParam(defaultValue = "1") Long nguoiDungId,
            RedirectAttributes redirectAttributes) {

        try {
            // Thiết lập người dùng ID
//            moHinh.setNguoiDungId(nguoiDungId);

            // Tạo mô hình mới
            MoHinhDaHuanLuyen moHinhMoi = moHinhService.taoMoHinh(moHinh);

            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo mô hình thành công!");
            return "redirect:/mo-hinh/" + moHinhMoi.getId();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xử lý: " + e.getMessage());
            return "redirect:/mo-hinh/them-moi";
        }
    }
}