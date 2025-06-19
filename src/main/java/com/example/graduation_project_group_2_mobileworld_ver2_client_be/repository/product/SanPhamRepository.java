package com.example.graduation_project_group_2_mobileworld_ver2_client_be.repository.product;

import com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product.SanPham;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query(value = """
    SELECT TOP 10
        sp.id AS id,
        sp.ten_san_pham AS tenSanPham,
        sp.created_at AS createdAt,
        nsx.id AS tenNhaSanXuat,
        ctsp.gia_ban AS giaBan,
        COALESCE(asp.duong_dan, '/assets/images/placeholder.jpg') AS imageUrl
    FROM 
        san_pham sp
    LEFT JOIN nha_san_xuat nsx ON sp.id_nha_san_xuat = nsx.id
    OUTER APPLY (
        SELECT TOP 1 ct.gia_ban, ct.id_anh_san_pham
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
        ORDER BY ct.created_at DESC
    ) ctsp
    LEFT JOIN anh_san_pham asp ON ctsp.id_anh_san_pham = asp.id
    WHERE EXISTS (
        SELECT 1
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
    ) AND sp.id > 10
    """, nativeQuery = true)
    List<Object[]> findProductsWithLatestVariant();


    @Query(value = """
    SELECT TOP 5
        sp.id AS id,
        sp.ten_san_pham AS tenSanPham,
        sp.created_at AS createdAt,
        nsx.id AS tenNhaSanXuat,
        ctsp.gia_ban AS giaBan,
        COALESCE(asp.duong_dan, '/assets/images/placeholder.jpg') AS imageUrl
    FROM 
        san_pham sp
    LEFT JOIN nha_san_xuat nsx ON sp.id_nha_san_xuat = nsx.id
    OUTER APPLY (
        SELECT TOP 1 ct.gia_ban, ct.id_anh_san_pham
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
        ORDER BY ct.created_at DESC
    ) ctsp
    LEFT JOIN anh_san_pham asp ON ctsp.id_anh_san_pham = asp.id
    WHERE EXISTS (
        SELECT 1
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
    ) AND (:idNhaSanXuat IS NULL OR nsx.id = :idNhaSanXuat)
    """, nativeQuery = true)
    List<Object[]> showNewProduct(@Param("idNhaSanXuat") Integer idNhaSanXuat);

    @Query(value = """
SELECT TOP 5
    sp.id AS id,
    sp.ten_san_pham AS tenSanPham,
    sp.created_at AS createdAt,
    nsx.id AS tenNhaSanXuat,
    COALESCE(ctdgg.gia_sau_khi_giam, ctsp.gia_ban) AS giaBan,
    COALESCE(asp.duong_dan, '/assets/images/placeholder.jpg') AS imageUrl
FROM 
    san_pham sp
LEFT JOIN nha_san_xuat nsx ON sp.id_nha_san_xuat = nsx.id
OUTER APPLY (
    SELECT TOP 1 ct.id, ct.gia_ban, ct.id_anh_san_pham
    FROM chi_tiet_san_pham ct
    WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
    ORDER BY ct.created_at DESC
) ctsp
LEFT JOIN anh_san_pham asp ON ctsp.id_anh_san_pham = asp.id
LEFT JOIN chi_tiet_dot_giam_gia ctdgg ON ctdgg.id_chi_tiet_san_pham = ctsp.id AND ctdgg.deleted = 0
LEFT JOIN (
    SELECT hdct.id_chi_tiet_san_pham, COUNT(*) AS so_luong_ban
    FROM hoa_don_chi_tiet hdct
    JOIN hoa_don hd ON hdct.id_hoa_don = hd.id
    WHERE hd.trang_thai = 1 AND hd.deleted = 0
    GROUP BY hdct.id_chi_tiet_san_pham
) sales ON sales.id_chi_tiet_san_pham = ctsp.id
WHERE EXISTS (
    SELECT 1
    FROM chi_tiet_san_pham ct
    WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
)
ORDER BY 
    CASE WHEN :sortBy = 'SALES' THEN COALESCE(sales.so_luong_ban, 0) ELSE 0 END DESC,
    CASE WHEN :sortBy = 'DISCOUNT' THEN CASE WHEN ctdgg.id IS NOT NULL THEN 1 ELSE 0 END ELSE 0 END DESC,
    CASE WHEN :sortBy NOT IN ('SALES', 'DISCOUNT') THEN sp.created_at END DESC,
    sp.created_at DESC
""", nativeQuery = true)
    List<Object[]> showBestProduct(@Param("sortBy") String sortBy);

    @Query(
            value = """
    SELECT
        sp.id AS id,
        sp.ten_san_pham AS tenSanPham,
        sp.created_at AS createdAt,
        nsx.id AS tenNhaSanXuat,
        ctsp.gia_ban AS giaBan,
        COALESCE(asp.duong_dan, '/assets/images/placeholder.jpg') AS imageUrl,
        (
            SELECT STRING_AGG(ms2.mau_sac, ',')
            FROM (
                SELECT DISTINCT ct2.id_mau_sac
                FROM chi_tiet_san_pham ct2
                WHERE ct2.id_san_pham = sp.id AND ct2.deleted = 0
            ) distinct_colors
            LEFT JOIN mau_sac ms2 ON distinct_colors.id_mau_sac = ms2.id
        ) AS mauSacList
    FROM 
        san_pham sp
    LEFT JOIN nha_san_xuat nsx ON sp.id_nha_san_xuat = nsx.id        
    OUTER APPLY (
        SELECT TOP 1 
            ct.id,
            ct.gia_ban,
            ct.id_anh_san_pham
        FROM chi_tiet_san_pham ct
        WHERE 
            ct.id_san_pham = sp.id 
            AND ct.deleted = 0
        ORDER BY ct.created_at DESC
    ) ctsp
    LEFT JOIN anh_san_pham asp ON ctsp.id_anh_san_pham = asp.id
    WHERE EXISTS (
        SELECT 1
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
    )
    GROUP BY
        sp.id,
        sp.ten_san_pham,
        sp.created_at,
        nsx.id,
        ctsp.gia_ban,
        asp.duong_dan
    """,
            countQuery = """
    SELECT COUNT(DISTINCT sp.id)
    FROM san_pham sp
    WHERE EXISTS (
        SELECT 1
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
    )
    """,
            nativeQuery = true
    )
    Page<Object[]> showAllProduct(Pageable pageable);

    @Query(value = """
    SELECT TOP 6
        sp.id AS id,
        sp.ten_san_pham AS tenSanPham,
        sp.created_at AS createdAt,
        nsx.id AS tenNhaSanXuat,
        ctsp.gia_ban AS giaBan,
        COALESCE(asp.duong_dan, '/assets/images/placeholder.jpg') AS imageUrl
    FROM 
        san_pham sp
    LEFT JOIN nha_san_xuat nsx ON sp.id_nha_san_xuat = nsx.id
    OUTER APPLY (
        SELECT TOP 1 ct.gia_ban, ct.id_anh_san_pham
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
        ORDER BY ct.created_at DESC
    ) ctsp
    LEFT JOIN anh_san_pham asp ON ctsp.id_anh_san_pham = asp.id
    WHERE EXISTS (
        SELECT 1
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
    ) AND sp.id > 10
    ORDER BY NEWID()
    """, nativeQuery = true)
    List<Object[]> suggestProductTop6();
}
