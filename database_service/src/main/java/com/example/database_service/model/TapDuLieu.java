package com.example.database_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tap_du_lieu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TapDuLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ten;

    @Column(columnDefinition = "TEXT")
    private String moTa;

    private String loai;


    @OneToMany(mappedBy = "tapDuLieu", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TapDuLieuMau> tapDuLieuMaus = new HashSet<>();

    @OneToMany(mappedBy = "tapDuLieu", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MoHinhDaHuanLuyen> danhSachMoHinhDaHuanLuyen;


}
