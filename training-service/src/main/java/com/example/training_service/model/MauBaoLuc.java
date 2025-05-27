package com.example.training_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MauBaoLuc {
    private Long id;
    private String ten;
    private String moTa;
    private String duongDanVideo;
    private String nhan;
//    private LocalDateTime ngayTao;
//    private LocalDateTime ngayCapNhat;

//    @JsonIgnore
//    private transient MultipartFile videoFile;
}
