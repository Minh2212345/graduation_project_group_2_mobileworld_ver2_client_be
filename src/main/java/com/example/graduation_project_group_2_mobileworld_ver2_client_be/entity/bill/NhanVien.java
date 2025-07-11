package com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.bill;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_nhan_vien")
    private String tenNhanVien;

    @Column(name = "ngay_sinh")
    private Instant ngaySinh;

    @Size(max = 255)
    @Nationalized
    @Column(name = "anh_nhan_vien")
    private String anhNhanVien;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Size(max = 255)
    @Nationalized
    @Column(name = "thanh_pho")
    private String thanhPho;

    @Size(max = 255)
    @Nationalized
    @Column(name = "quan")
    private String quan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "phuong")
    private String phuong;

    @Size(max = 255)
    @Nationalized
    @Column(name = "dia_chi_cu_the")
    private String diaChiCuThe;

    @Size(max = 255)
    @Nationalized
    @Column(name = "cccd")
    private String cccd;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

}