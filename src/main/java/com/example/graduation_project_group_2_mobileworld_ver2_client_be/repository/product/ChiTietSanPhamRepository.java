package com.example.graduation_project_group_2_mobileworld_ver2_client_be.repository.product;

import com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {
    @Query(value = """
    SELECT 
        sp.id AS sp_id,
        sp.ten_san_pham,
        sp.ma AS sp_ma,
        sp.created_at AS sp_created_at,
        nsx.nha_san_xuat AS ten_nha_san_xuat,
        cpu.ten_cpu,
        gpu.ten_gpu,
        cc.thong_so_camera_sau,
        cc.thong_so_camera_truoc,
        ctsp.id AS ctsp_id,
        ctsp.gia_ban,
        ctsp.ma AS ctsp_ma,
        ctsp.id_imel,
        ms.mau_sac,
        ram.dung_luong_ram AS ram_dung_luong,
        bnt.dung_luong_bo_nho_trong AS bo_nho_trong_dung_luong,
        COALESCE(asp.duong_dan, '/assets/images/placeholder.jpg') AS anh_san_pham_url,
        ctsp.ghi_chu,
        COALESCE(ctdgg.gia_sau_khi_giam, ctsp.gia_ban) AS gia_sau_khi_giam,
        ctdgg.gia_ban_dau AS gia_ban_dau,
        CASE WHEN ctdgg.id IS NOT NULL THEN 1 ELSE 0 END AS has_discount,
        dgg.gia_tri_giam_gia AS giam_phan_tram,
        dgg.so_tien_giam_toi_da AS giam_toi_da,
        COALESCE(dgg.loai_giam_gia_ap_dung, 'NONE') AS loai_giam_gia_ap_dung,
        cskbn.ten_chi_so AS chi_so_khang_bui_nuoc,
        cnm.ten_cong_nghe_mang,
        hd.he_dieu_hanh,
        hd.phien_ban,
        htbnn.ho_tro_bo_nho_ngoai,
        p.dung_luong_pin,
        s.cac_loai_sim_ho_tro,
        tk.chat_lieu_khung,
        tk.chat_lieu_mat_lung,
        htcs.cong_nghe_ho_tro,
        cnmh.cong_nghe_man_hinh,
        cnmh.chuan_man_hinh,
        cnmh.kich_thuoc,
        cnmh.do_phan_giai,
        cnmh.do_sang_toi_da,
        cnmh.tan_so_quet,
        cnmh.kieu_man_hinh
    FROM 
        san_pham sp
    LEFT JOIN nha_san_xuat nsx ON sp.id_nha_san_xuat = nsx.id
    LEFT JOIN cpu ON sp.id_cpu = cpu.id
    LEFT JOIN gpu ON sp.id_gpu = gpu.id
    LEFT JOIN cum_camera cc ON sp.id_cum_camera = cc.id
    LEFT JOIN chi_so_khang_bui_va_nuoc cskbn ON sp.id_chi_so_khang_bui_va_nuoc = cskbn.id
    LEFT JOIN cong_nghe_mang cnm ON sp.id_cong_nghe_mang = cnm.id
    LEFT JOIN he_dieu_hanh hd ON sp.id_he_dieu_hanh = hd.id
    LEFT JOIN ho_tro_bo_nho_ngoai htbnn ON sp.id_ho_tro_bo_nho_ngoai = htbnn.id
    LEFT JOIN pin p ON sp.id_pin = p.id
    LEFT JOIN sim s ON sp.id_sim = s.id
    LEFT JOIN thiet_ke tk ON sp.id_thiet_ke = tk.id
    LEFT JOIN ho_tro_cong_nghe_sac htcs ON sp.ho_tro_cong_nghe_sac_id = htcs.id
    LEFT JOIN cong_nghe_man_hinh cnmh ON sp.cong_nghe_man_hinh_id = cnmh.id
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
            ghi_chu,
            created_at
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
                ct.created_at,
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
    LEFT JOIN chi_tiet_dot_giam_gia ctdgg ON ctsp.id = ctdgg.id_chi_tiet_san_pham AND ctdgg.deleted = 0
    LEFT JOIN dot_giam_gia dgg ON ctdgg.id_dot_giam_gia = dgg.id
    WHERE 
        sp.id = :sanPhamId 
        AND sp.deleted = 0
""", nativeQuery = true)
    List<Object[]> findChiTietSanPhamBySanPhamId(@Param("sanPhamId") Integer sanPhamId);
    @Query("SELECT MIN(ctsp.giaBan) FROM ChiTietSanPham ctsp WHERE ctsp.deleted = false")
    Double findMinPrice();

    @Query("SELECT MAX(ctsp.giaBan) FROM ChiTietSanPham ctsp WHERE ctsp.deleted = false")
    Double findMaxPrice();

    @Query("SELECT DISTINCT ctsp.idMauSac.mauSac FROM ChiTietSanPham ctsp WHERE ctsp.deleted = false")
    List<String> findDistinctColors();

}
