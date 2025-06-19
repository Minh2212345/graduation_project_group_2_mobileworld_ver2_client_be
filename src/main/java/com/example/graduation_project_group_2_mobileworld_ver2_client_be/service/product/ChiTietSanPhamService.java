package com.example.graduation_project_group_2_mobileworld_ver2_client_be.service.product;

import com.example.graduation_project_group_2_mobileworld_ver2_client_be.entity.product.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld_ver2_client_be.repository.product.ChiTietSanPhamRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietSanPhamService {
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    public ChiTietSanPhamService(ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    public List<Object[]> findChiTietSanPhamBySanPhamId(Integer sanPhamId){
        return chiTietSanPhamRepository.findChiTietSanPhamBySanPhamId(sanPhamId);
    }
}
