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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mau-bao-luc")
public class MauBaoLucController {

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
            System.out.println(result.get("mauBaoLucs"));

            model.addAttribute("mauBaoLucs", result.get("mauBaoLucs"));
            model.addAttribute("trangHienTai", result.get("trangHienTai"));
            model.addAttribute("tongSoMuc", result.get("tongSoMuc"));
            model.addAttribute("tongSoTrang", result.get("tongSoTrang"));
            model.addAttribute("nhan", nhan);

            System.out.println("list-mau-bao-luc-page");
            return "mau-bao-luc/list";
        }catch (Exception e){
            System.out.println(e.toString());
            return "redirect:/error";
        }

    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        MauBaoLuc mauBaoLuc = mauBaoLucService.layMauBaoLucTheoId(id);
        model.addAttribute("mauBaoLuc", mauBaoLuc);

        return "mau-bao-luc/detail";
    }

    // Tiếp tục MauBaoLucController.java
    @GetMapping("/them-moi")
    public String formThemMoi(Model model) {
        model.addAttribute("mauBaoLuc", new MauBaoLuc());
        System.out.println("them-moi-mau-bao-luc-page");
        return "mau-bao-luc/add";
    }

    @PostMapping("/them-moi")
    public String themMoi(@ModelAttribute MauBaoLuc mauBaoLuc) {
        try {
            System.out.println("Lay du lieu");
            mauBaoLucService.taoMauBaoLuc(mauBaoLuc);
            System.out.println("lay du lieu xong");
            return "redirect:/mau-bao-luc";
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Error creating MauBaoLuc: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/chinh-sua/{id}")
    public String formChinhSua(@PathVariable Long id, Model model) {
        MauBaoLuc mauBaoLuc = mauBaoLucService.layMauBaoLucTheoId(id);
        model.addAttribute("mauBaoLuc", mauBaoLuc);

        return "mau-bao-luc/edit";
    }

    @PostMapping("/chinh-sua/{id}")
    public String chinhSua(@PathVariable Long id, @ModelAttribute MauBaoLuc mauBaoLuc) {
        try {
            mauBaoLucService.capNhatMauBaoLuc(id, mauBaoLuc);
            return "redirect:/mau-bao-luc/" + id;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id) {
        try {
            mauBaoLucService.xoaMauBaoLuc(id);
            return "redirect:/mau-bao-luc";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/nhan/{nhan}")
    public String locTheoNhan(@PathVariable String nhan, Model model) {
        List<MauBaoLuc> mauBaoLucs = mauBaoLucService.layMauBaoLucTheoNhan(nhan);
        model.addAttribute("mauBaoLucs", mauBaoLucs);
        model.addAttribute("nhan", nhan);

        return "mau-bao-luc/list-by-nhan";
    }
}