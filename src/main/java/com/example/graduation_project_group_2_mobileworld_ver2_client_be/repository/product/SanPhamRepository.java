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
            ct.id_anh_san_pham,
            r.dung_luong_ram,
            bnt.dung_luong_bo_nho_trong,
            cc.thong_so_camera_sau
        FROM chi_tiet_san_pham ct
        LEFT JOIN ram r ON ct.id_ram = r.id
        LEFT JOIN bo_nho_trong bnt ON ct.id_bo_nho_trong = bnt.id
        LEFT JOIN cum_camera cc ON ct.id_san_pham = (SELECT sp2.id FROM san_pham sp2 WHERE sp2.id_cum_camera = cc.id AND sp2.id = ct.id_san_pham)
        WHERE 
            ct.id_san_pham = sp.id 
            AND ct.deleted = 0
            AND (:useCases = '' OR (
                (:useCases LIKE '%bo_nho_lon%' AND ct.id_bo_nho_trong >= 4) OR
                (:useCases LIKE '%pin_trau%' AND (SELECT TOP 1 sp2.id_pin FROM san_pham sp2 WHERE sp2.id = ct.id_san_pham) >= 23) OR
                (:useCases LIKE '%ram_lon%' AND ct.id_ram >= 4) OR
                (:useCases LIKE '%cau_hinh_cao%' AND ct.gia_ban > 25000000) OR
                (:useCases LIKE '%chup_anh_dep%' AND cc.thong_so_camera_sau LIKE '%,%,%')
            ))
            AND (:colors = '' OR EXISTS (
                SELECT 1 FROM chi_tiet_san_pham ct2
                JOIN mau_sac ms ON ct2.id_mau_sac = ms.id
                WHERE ct2.id_san_pham = sp.id AND ct2.deleted = 0
                AND ms.mau_sac IN (SELECT value FROM STRING_SPLIT(:colors, ','))
            ))
            AND ct.gia_ban >= :minPrice
            AND (:maxPrice = 0 OR ct.gia_ban <= :maxPrice)
        ORDER BY ct.created_at DESC
    ) ctsp
    LEFT JOIN anh_san_pham asp ON ctsp.id_anh_san_pham = asp.id
    WHERE EXISTS (
        SELECT 1
        FROM chi_tiet_san_pham ct
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
        AND (:brands = '' OR nsx.id IN (SELECT value FROM STRING_SPLIT(:brands, ',')))
    )
    AND ctsp.id IS NOT NULL
    GROUP BY
        sp.id,
        sp.ten_san_pham,
        sp.created_at,
        nsx.id,
        ctsp.gia_ban,
        asp.duong_dan
    ORDER BY
        CASE 
            WHEN :sortBy = 'popularity' THEN sp.created_at
            ELSE NULL
        END DESC,
        CASE 
            WHEN :sortBy = 'price-desc' THEN ctsp.gia_ban
            ELSE NULL
        END DESC,
        CASE 
            WHEN :sortBy = 'price-asc' THEN ctsp.gia_ban
            ELSE NULL
        END ASC,
        sp.created_at DESC
    """,
            countQuery = """
    SELECT COUNT(DISTINCT sp.id)
    FROM san_pham sp
    LEFT JOIN nha_san_xuat nsx ON sp.id_nha_san_xuat = nsx.id
    WHERE EXISTS (
        SELECT 1
        FROM chi_tiet_san_pham ct
        LEFT JOIN ram r ON ct.id_ram = r.id
        LEFT JOIN bo_nho_trong bnt ON ct.id_bo_nho_trong = bnt.id
        LEFT JOIN cum_camera cc ON ct.id_san_pham = (SELECT sp2.id FROM san_pham sp2 WHERE sp2.id_cum_camera = cc.id AND sp2.id = ct.id_san_pham)
        WHERE ct.id_san_pham = sp.id AND ct.deleted = 0
        AND (:useCases = '' OR (
            (:useCases LIKE '%bo_nho_lon%' AND ct.id_bo_nho_trong >= 4) OR
            (:useCases LIKE '%pin_trau%' AND (SELECT TOP 1 sp2.id_pin FROM san_pham sp2 WHERE sp2.id = ct.id_san_pham) >= 23) OR
            (:useCases LIKE '%ram_lon%' AND ct.id_ram >= 4) OR
            (:useCases LIKE '%cau_hinh_cao%' AND ct.gia_ban > 25000000) OR
            (:useCases LIKE '%chup_anh_dep%' AND cc.thong_so_camera_sau LIKE '%,%,%')
        ))
        AND (:colors = '' OR EXISTS (
            SELECT 1 FROM chi_tiet_san_pham ct2
            JOIN mau_sac ms ON ct2.id_mau_sac = ms.id
            WHERE ct2.id_san_pham = sp.id AND ct2.deleted = 0
            AND ms.mau_sac IN (SELECT value FROM STRING_SPLIT(:colors, ','))
        ))
        AND (:brands = '' OR nsx.id IN (SELECT value FROM STRING_SPLIT(:brands, ',')))
        AND ct.gia_ban >= :minPrice
        AND (:maxPrice = 0 OR ct.gia_ban <= :maxPrice)
    )
    """,
            nativeQuery = true
    )
    Page<Object[]> showAllProduct(Pageable pageable, @Param("sortBy") String sortBy, @Param("useCases") String useCases, @Param("colors") String colors, @Param("brands") String brands, @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);
}
