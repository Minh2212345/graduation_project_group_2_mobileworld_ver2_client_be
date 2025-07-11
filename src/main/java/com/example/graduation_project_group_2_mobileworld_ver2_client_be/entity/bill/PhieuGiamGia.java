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

@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_phieu_giam_gia")
    private String tenPhieuGiamGia;

    @Size(max = 255)
    @Nationalized
    @Column(name = "loai_phieu_giam_gia")
    private String loaiPhieuGiamGia;

    @Column(name = "phan_tram_giam_gia")
    private Double phanTramGiamGia;

    @Column(name = "so_tien_giam_toi_da")
    private Double soTienGiamToiDa;

    @Column(name = "hoa_don_toi_thieu")
    private Double hoaDonToiThieu;

    @Column(name = "so_luong_dung")
    private Integer soLuongDung;

    @Column(name = "ngay_bat_dau")
    private Instant ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Instant ngayKetThuc;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "rieng_tu")
    private Boolean riengTu;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "deleted")
    private Boolean deleted;

}