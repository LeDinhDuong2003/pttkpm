package com.example.mau_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MauBaoLuc {
    private Long id;
    private String ten;
    private String moTa;
    private String duongDanVideo;
    private String nhan;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    // Không lưu trong DB, chỉ để xử lý upload file
    @JsonIgnore
    private transient MultipartFile videoFile;
}
