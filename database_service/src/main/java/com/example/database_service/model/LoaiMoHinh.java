package com.example.database_service.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loai_mo_hinh")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiMoHinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ten;

    @Column(columnDefinition = "TEXT")
    private String moTa;

    @Column(columnDefinition = "TEXT")
    private String thongSoMacDinh;

    @OneToMany(mappedBy = "loaiMoHinh", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MoHinhDaHuanLuyen> danhSachMoHinhDaHuanLuyen;
}
