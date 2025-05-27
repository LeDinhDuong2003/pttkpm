package com.example.mau_service.controller;

import com.example.mau_service.model.MauBaoLuc;
import com.example.mau_service.service.MauBaoLucService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mau-bao-luc")
public class MauBaoLucController {

    private static final Logger logger = LoggerFactory.getLogger(MauBaoLucController.class);
    private final MauBaoLucService mauBaoLucService;

    @Value("${api.database-service.url}")
    private String apiBaseUrl;

    public MauBaoLucController(MauBaoLucService mauBaoLucService) {
        this.mauBaoLucService = mauBaoLucService;
    }

    @GetMapping
    public String danhSach(
            @RequestParam(required = false) String nhan,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int soLuong,
            Model model) {
        try{
            Map<String, Object> result = mauBaoLucService.layDanhSachMauBaoLuc(nhan, trang, soLuong);
            List<MauBaoLuc> mauBaoLucs = (List<MauBaoLuc>) result.get("mauBaoLucs");
            logger.debug("Lấy được {} mẫu bạo lực", mauBaoLucs != null ? mauBaoLucs.size() : 0);

            model.addAttribute("mauBaoLucs", result.get("mauBaoLucs"));
            model.addAttribute("trangHienTai", result.get("trangHienTai"));
            model.addAttribute("tongSoMuc", result.get("tongSoMuc"));
            model.addAttribute("tongSoTrang", result.get("tongSoTrang"));
            model.addAttribute("nhan", nhan);

            logger.info("Hiển thị trang danh sách mẫu bạo lực");
            return "mau-bao-luc/list";
        } catch (Exception e) {
            logger.error("Lỗi khi hiển thị danh sách mẫu bạo lực: {}", e.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        try {
            MauBaoLuc mauBaoLuc = mauBaoLucService.layMauBaoLucTheoId(id);
            model.addAttribute("mauBaoLuc", mauBaoLuc);
            logger.info("Hiển thị chi tiết mẫu bạo lực ID: {}", id);
            return "mau-bao-luc/detail";
        } catch (Exception e) {
            logger.error("Lỗi khi hiển thị chi tiết mẫu bạo lực: {}", e.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/them-moi")
    public String formThemMoi(Model model) {
        model.addAttribute("mauBaoLuc", new MauBaoLuc());
        logger.info("Hiển thị form thêm mới mẫu bạo lực");
        return "mau-bao-luc/add";
    }

    @PostMapping("/them-moi")
    public String themMoi(@ModelAttribute MauBaoLuc mauBaoLuc,
                          @RequestParam("videoFile") MultipartFile videoFile) {
        try {
            logger.info("Bắt đầu xử lý thêm mới mẫu bạo lực");


            // Kiểm tra file có được upload không
            if (videoFile != null && !videoFile.isEmpty()) {
                logger.info("File video được upload: {} ({} bytes)",
                        videoFile.getOriginalFilename(), videoFile.getSize());
            } else {
                logger.warn("Không có file video được upload");
            }

            // Lưu mẫu bạo lực (service sẽ xử lý việc tải file lên)
            MauBaoLuc savedMau = mauBaoLucService.taoMauBaoLuc(mauBaoLuc,videoFile);
            logger.info("Đã tạo mẫu bạo lực ID: {}", savedMau.getId());

            return "redirect:/mau-bao-luc";
        } catch (Exception e) {
            logger.error("Lỗi khi tạo mẫu bạo lực: {}", e.getMessage(), e);
            return "redirect:/error";
        }
    }

    @GetMapping("/chinh-sua/{id}")
    public String formChinhSua(@PathVariable Long id, Model model) {
        try {
            MauBaoLuc mauBaoLuc = mauBaoLucService.layMauBaoLucTheoId(id);
            model.addAttribute("mauBaoLuc", mauBaoLuc);
            logger.info("Hiển thị form chỉnh sửa mẫu bạo lực ID: {}", id);
            return "mau-bao-luc/edit";
        } catch (Exception e) {
            logger.error("Lỗi khi hiển thị form chỉnh sửa: {}", e.getMessage());
            return "redirect:/error";
        }
    }

    @PostMapping("/chinh-sua/{id}")
    public String chinhSua(@PathVariable Long id,
                           @ModelAttribute MauBaoLuc mauBaoLuc,
                           @RequestParam(value = "videoFile", required = false) MultipartFile videoFile) {
        try {
            logger.info("Bắt đầu xử lý chỉnh sửa mẫu bạo lực ID: {}", id);

            // Gán file video vào model để dịch vụ có thể xử lý
//            mauBaoLuc.setVideoFile(videoFile);

            // Kiểm tra file có được upload không
            if (videoFile != null && !videoFile.isEmpty()) {
                logger.info("File video mới được upload: {} ({} bytes)",
                        videoFile.getOriginalFilename(), videoFile.getSize());
            } else {
                logger.info("Không thay đổi file video");
            }

            // Cập nhật mẫu bạo lực
            mauBaoLucService.capNhatMauBaoLuc(id, mauBaoLuc,videoFile);
            logger.info("Đã cập nhật mẫu bạo lực ID: {}", id);

            return "redirect:/mau-bao-luc/" + id;
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật mẫu bạo lực: {}", e.getMessage(), e);
            return "redirect:/error";
        }
    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id) {
        try {
            logger.info("Xóa mẫu bạo lực ID: {}", id);
            mauBaoLucService.xoaMauBaoLuc(id);
            return "redirect:/mau-bao-luc";
        } catch (Exception e) {
            logger.error("Lỗi khi xóa mẫu bạo lực: {}", e.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/nhan/{nhan}")
    public String locTheoNhan(@PathVariable String nhan, Model model) {
        try {
            List<MauBaoLuc> mauBaoLucs = mauBaoLucService.layMauBaoLucTheoNhan(nhan);
            model.addAttribute("mauBaoLucs", mauBaoLucs);
            model.addAttribute("nhan", nhan);
            logger.info("Lọc mẫu bạo lực theo nhãn: {}", nhan);
            return "mau-bao-luc/list-by-nhan";
        } catch (Exception e) {
            logger.error("Lỗi khi lọc theo nhãn: {}", e.getMessage());
            return "redirect:/error";
        }
    }
}