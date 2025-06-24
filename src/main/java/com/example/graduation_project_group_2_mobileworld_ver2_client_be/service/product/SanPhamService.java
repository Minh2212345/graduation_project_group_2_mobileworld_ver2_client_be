package com.example.graduation_project_group_2_mobileworld_ver2_client_be.service.product;

import com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product.SanPham;
import com.example.graduation_project_group_2_mobileworld_ver2_client_be.repository.product.SanPhamRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamService {
    private SanPhamRepository sanPhamRepository;

    @Autowired
    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    public List<Object[]> findProductsWithLatestVariant(@Param("idNhaSanXuat") Integer idNhaSanXuat) {
        return sanPhamRepository.findProductsWithLatestVariant(idNhaSanXuat);
    }

    public List<Object[]> showNewProduct(@Param("idNhaSanXuat") Integer idNhaSanXuat) {
        return sanPhamRepository.showNewProduct(idNhaSanXuat);
    }

    public List<Object[]> showBestProduct(@Param("sortBy") String sortBy) {
        return sanPhamRepository.showBestProduct(sortBy);
    }

    public Page<Object[]> showAllProduct(Pageable pageable){
        return sanPhamRepository.showAllProduct(pageable);
    }

    public List<Object[]> suggestProductTop6(){
        return sanPhamRepository.suggestProductTop6();
    }

    public Page<Object[]> showAllProduct(Pageable pageable,  String sortBy,String useCases, String colors, String brands, double minPrice, double maxPrice) {
        return sanPhamRepository.showAllProduct(pageable, sortBy,useCases, colors, brands, minPrice, maxPrice);
    }
}
