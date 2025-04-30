package com.example.database_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mau_bao_luc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MauBaoLuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ten;

    @Column(columnDefinition = "TEXT")
    private String moTa;

    private String duongDanVideo;

    private String nhan;

    @OneToMany(mappedBy = "mauBaoLuc", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TapDuLieuMau> tapDuLieuMaus = new HashSet<>();
}
