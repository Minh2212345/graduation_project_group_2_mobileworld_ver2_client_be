package com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "mau_sac")
public class MauSac {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mau_sac")
    private String mauSac;

    @Column(name = "deleted")
    private Boolean deleted;

    @Size(max = 20)
    @Nationalized
    @Column(name = "ma_mau", length = 20)
    private String maMau;

}