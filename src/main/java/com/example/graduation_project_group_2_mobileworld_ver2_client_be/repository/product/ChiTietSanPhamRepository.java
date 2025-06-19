package com.example.graduation_project_group_2_mobileworld_ver2_client_be.repository.product;

import com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {
    @Query(value = """
    SELECT 
        sp.id AS sp_id,
        sp.ten_san_pham,
        sp.ma AS sp_ma,
        sp.created_at AS sp_created_at,
        nsx.nha_san_xuat AS nha_san_xuat,
        cpu.ten_cpu,
        gpu.ten_gpu,
        cc.thong_so_camera_sau,
        cc.thong_so_camera_truoc,
        ctsp.id AS ctsp_id,
        ctsp.gia_ban,
        ctsp.ma AS ctsp_ma,
        ctsp.id_imel,
        ms.mau_sac AS mau_sac,
        ram.dung_luong_ram AS ram_dung_luong,
        bnt.dung_luong_bo_nho_trong AS bo_nho_trong_dung_luong,
        COALESCE(asp.duong_dan, '/assets/images/placeholder.jpg') AS anh_san_pham_url,
        ctsp.ghi_chu AS ghi_chu
    FROM 
        san_pham sp
    LEFT JOIN nha_san_xuat nsx ON sp.id_nha_san_xuat = nsx.id
    LEFT JOIN cpu ON sp.id_cpu = cpu.id
    LEFT JOIN gpu ON sp.id_gpu = gpu.id
    LEFT JOIN cum_camera cc ON sp.id_cum_camera = cc.id
    OUTER APPLY (
        SELECT 
            id,
            gia_ban,
            ma,
            id_imel,
            id_mau_sac,
            id_ram,
            id_bo_nho_trong,
            id_anh_san_pham,
            ghi_chu
        FROM (
            SELECT 
                ct.id,
                ct.gia_ban,
                ct.ma,
                ct.id_imel,
                ct.id_mau_sac,
                ct.id_ram,
                ct.id_bo_nho_trong,
                ct.id_anh_san_pham,
                ct.ghi_chu,
                ROW_NUMBER() OVER (
                    PARTITION BY ct.id_mau_sac, ct.id_bo_nho_trong 
                    ORDER BY ct.created_at DESC
                ) AS rn
            FROM chi_tiet_san_pham ct
            WHERE 
                ct.id_san_pham = :sanPhamId 
                AND ct.deleted = 0
        ) ct
        WHERE rn = 1
    ) ctsp
    LEFT JOIN mau_sac ms ON ctsp.id_mau_sac = ms.id
    LEFT JOIN ram ON ctsp.id_ram = ram.id
    LEFT JOIN bo_nho_trong bnt ON ctsp.id_bo_nho_trong = bnt.id
    LEFT JOIN anh_san_pham asp ON ctsp.id_anh_san_pham = asp.id
    WHERE 
        sp.id = :sanPhamId 
        AND sp.deleted = 0
    """, nativeQuery = true)
    List<Object[]> findChiTietSanPhamBySanPhamId(@Param("sanPhamId") Integer sanPhamId);
}
