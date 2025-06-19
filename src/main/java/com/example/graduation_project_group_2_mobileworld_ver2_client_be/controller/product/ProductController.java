package com.example.graduation_project_group_2_mobileworld_ver2_client_be.controller.product;

import com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product.SanPham;
import com.example.graduation_project_group_2_mobileworld_ver2_client_be.repository.product.ChiTietSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld_ver2_client_be.repository.product.SanPhamRepository;
import com.example.graduation_project_group_2_mobileworld_ver2_client_be.service.product.ChiTietSanPhamService;
import com.example.graduation_project_group_2_mobileworld_ver2_client_be.service.product.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    public ProductController(
            SanPhamService sanPhamService,
            ChiTietSanPhamService chiTietSanPhamService,
            ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.sanPhamService = sanPhamService;
        this.chiTietSanPhamService = chiTietSanPhamService;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    // Get product variants by sanPhamId
    @GetMapping("/chi-tiet-san-pham")
    public List<Map<String, Object>> getProductVariants(@RequestParam("sanPhamId") Integer sanPhamId) {
        List<Object[]> results = chiTietSanPhamRepository.findChiTietSanPhamBySanPhamId(sanPhamId);
        return results.stream().map(record -> {
            Map<String, Object> variant = new HashMap<>();
            variant.put("sp_id", record[0]); // sp.id
            variant.put("ten_san_pham", record[1]); // sp.ten_san_pham
            variant.put("sp_ma", record[2]); // sp.ma
            variant.put("sp_created_at", record[3]); // sp.created_at
            variant.put("nha_san_xuat", record[4]); // nsx.ten_nha_san_xuat
            variant.put("ten_cpu", record[5]); // cpu.ten_cpu
            variant.put("ten_gpu", record[6]); // gpu.ten_gpu
            variant.put("thong_so_camera_sau", record[7]); // cc.thong_so_camera_sau
            variant.put("thong_so_camera_truoc", record[8]); // cc.thong_so_camera_truoc
            variant.put("ctsp_id", record[9]); // ctsp.id
            variant.put("gia_ban", record[10] != null ? record[10] : 0); // ctsp.gia_ban
            variant.put("ctsp_ma", record[11]); // ctsp.ma
            variant.put("id_imel", record[12]); // ctsp.id_imel
            variant.put("mau_sac", record[13]); // ms.ten_mau_sac
            variant.put("ram_dung_luong", record[14]); // ram.dung_luong
            variant.put("bo_nho_trong_dung_luong", record[15]); // bnt.dung_luong
            variant.put("anh_san_pham_url", record[16] != null ? record[16] : "/assets/images/placeholder.jpg"); // asp.duong_dan
            variant.put("ghi_chu", record[17] != null ? record[17] : "Không có mô tả chi tiết.");
            return variant;
        }).collect(Collectors.toList());
    }

    // Get suggested products (top 6)
    @GetMapping("/suggested-products")
    public List<Map<String, Object>> getSuggestedProducts() {
        List<Object[]> results = sanPhamService.suggestProductTop6();
        return results.stream().map(record -> {
            Map<String, Object> product = new HashMap<>();
            product.put("id", record[0]); // sp.id
            product.put("tenSanPham", record[1]); // sp.ten_san_pham
            product.put("createdAt", record[2]); // sp.created_at
            product.put("tenNhaSanXuat", record[3]); // nsx.id or nsx.ten_nha_san_xuat
            product.put("giaBan", record[4] != null ? record[4] : 0); // ctsp.gia_ban
            product.put("imageUrl", record[5] != null ? record[5] : "/assets/images/placeholder.jpg"); // asp.duong_dan
            return product;
        }).collect(Collectors.toList());
    }

    // Get product variants
    @GetMapping("/san-pham-with-variants")
    public List<Map<String, Object>> getProductsWithLatestVariant() {
        List<Object[]> results = sanPhamService.findProductsWithLatestVariant();
        return results.stream().map(record -> {
            Map<String, Object> product = new HashMap<>();
            product.put("id", record[0]); // sp.id
            product.put("tenSanPham", record[1]); // sp.ten_san_pham
            product.put("createdAt", record[2]); // sp.created_at
            product.put("tenNhaSanXuat", record[3]); // nsx.id (ID nhà sản xuất)
            product.put("giaBan", record[4] != null ? record[4] : 0); // ctsp.gia_ban
            product.put("imageUrl", record[5] != null ? record[5] : "/assets/images/placeholder.jpg"); // asp.duong_dan
            return product;
        }).collect(Collectors.toList());
    }

    @GetMapping("/show-new-product")
    public List<Map<String, Object>> getNewProducts(@RequestParam(required = false) Integer idNhaSanXuat) {
        List<Object[]> results = sanPhamService.showNewProduct(idNhaSanXuat);
        return results.stream().map(record -> {
            Map<String, Object> product = new HashMap<>();
            product.put("id", record[0]); // sp.id
            product.put("tenSanPham", record[1]); // sp.ten_san_pham
            product.put("createdAt", record[2]); // sp.created_at
            product.put("tenNhaSanXuat", record[3]); // nsx.id (ID nhà sản xuất)
            product.put("giaBan", record[4] != null ? record[4] : 0); // ctsp.gia_ban
            product.put("imageUrl", record[5] != null ? record[5] : "/assets/images/placeholder.jpg"); // asp.duong_dan
            return product;
        }).collect(Collectors.toList());
    }

    @GetMapping("/show-best-product")
    public List<Map<String, Object>> getBestProducts(@RequestParam(required = false) String sortBy) {
        List<Object[]> results = sanPhamService.showBestProduct(sortBy != null ? sortBy : "RATING");
        return results.stream().map(record -> {
            Map<String, Object> product = new HashMap<>();
            product.put("id", record[0]); // sp.id
            product.put("tenSanPham", record[1]); // sp.ten_san_pham
            product.put("createdAt", record[2]); // sp.created_at
            product.put("tenNhaSanXuat", record[3]); // nsx.id (ID nhà sản xuất)
            product.put("giaBan", record[4] != null ? record[4] : 0); // ctsp.gia_ban or ctdgg.gia_sau_khi_giam
            product.put("imageUrl", record[5] != null ? record[5] : "/assets/images/placeholder.jpg"); // asp.duong_dan
            return product;
        }).collect(Collectors.toList());
    }

    // Get all products with pagination
    @GetMapping("/products")
    public Map<String, Object> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> productPage = sanPhamService.showAllProduct(pageable);

        List<Map<String, Object>> products = productPage.getContent().stream().map(record -> {
            Map<String, Object> product = new HashMap<>();
            product.put("id", record[0]); // sp.id
            product.put("tenSanPham", record[1]); // sp.ten_san_pham
            product.put("createdAt", record[2]); // sp.created_at
            product.put("tenNhaSanXuat", record[3]); // nsx.id
            product.put("giaBan", record[4] != null ? record[4] : 0); // ctsp.gia_ban
            product.put("imageUrl", record[5]); // asp.duong_dan
            product.put("mauSacList", record[6] != null ? Arrays.asList(((String) record[6]).split(",")) : Collections.emptyList()); // ms.mau_sac_list
            return product;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());

        return response;
    }
}