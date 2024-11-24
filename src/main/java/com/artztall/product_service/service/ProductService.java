package com.artztall.product_service.service;

import com.artztall.product_service.dto.ProductRequest;
import com.artztall.product_service.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest product, String artistId);
    ProductResponse updateProduct(String id, ProductRequest product);

    void deleteProduct(String id);
    ProductResponse getProductById(String id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    List<ProductResponse> getProductsByArtist(String artistId);
    List<ProductResponse> getProductsByCategory(String category);
    List<ProductResponse> getProductsByPriceRange(Double minPrice, Double maxPrice);
    ProductResponse updateProductAvailability(String id, boolean available);
    ProductResponse reserveProduct(String productId);
    ProductResponse releaseProduct(String productId);

}
