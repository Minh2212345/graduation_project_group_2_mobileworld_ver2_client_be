package com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "anh_san_pham")
public class AnhSanPham {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_anh", nullable = false)
    private String tenAnh;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "duong_dan", nullable = false)
    private String duongDan;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Size(max = 255)
    @Column(name = "hash")
    private String hash;

    @Size(max = 255)
    @Column(name = "product_group_key")
    private String productGroupKey;

}