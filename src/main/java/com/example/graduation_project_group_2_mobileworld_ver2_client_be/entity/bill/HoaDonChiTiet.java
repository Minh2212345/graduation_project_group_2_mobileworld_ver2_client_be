package com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.bill;

import com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product.ChiTietSanPham;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "hoa_don_chi_tiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_hoa_don", nullable = false)
    private HoaDon idHoaDon;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_chi_tiet_san_pham", nullable = false)
    private ChiTietSanPham idChiTietSanPham;

    @ManyToOne
    @JoinColumn(name = "id_imel_da_ban")
    private ImelDaBan idImelDaBan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @NotNull
    @Column(name = "gia", nullable = false, precision = 18, scale = 2)
    private BigDecimal gia;

    @Column(name = "trang_thai", columnDefinition = "tinyint not null")
    private Short trangThai;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

}