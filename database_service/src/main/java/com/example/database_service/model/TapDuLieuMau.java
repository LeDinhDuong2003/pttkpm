package com.example.database_service.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tap_du_lieu_mau")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TapDuLieuMau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tap_du_lieu_id")
    private TapDuLieu tapDuLieu;

    @ManyToOne
    @JoinColumn(name = "mau_bao_luc_id")
    private MauBaoLuc mauBaoLuc;

}
