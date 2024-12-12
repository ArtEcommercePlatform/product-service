package com.artztall.product_service.service;

import com.artztall.product_service.dto.ProductDimensionsDTO;
import com.artztall.product_service.dto.ProductRequest;
import com.artztall.product_service.dto.ProductResponse;
import com.artztall.product_service.model.Product;
import com.artztall.product_service.model.ProductDimensions;
import com.artztall.product_service.repository.ProductRepository;
import jakarta.inject.Inject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public ProductResponse createProduct(ProductRequest productRequest, String artistId){
        Product product = mapToProduct(productRequest);
        product.setArtistId(artistId);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setAvailable(true);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse((savedProduct));
    }

    @Override
    public ProductResponse updateProduct(String id, ProductRequest productRequest){
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product Not Found"));

        updateProduct(existingProduct, productRequest);
        Product updatedProduct = productRepository.save(existingProduct);
        return mapToProductResponse(updatedProduct);
    }


    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToProductResponse(product);
    }


    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToProductResponse);
    }

    @Override
    public List<ProductResponse> getProductsByArtist(String artistId) {
        return productRepository.findByArtistId(artistId).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProductAvailability(String id, boolean available) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setAvailable(available);
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        return mapToProductResponse(updatedProduct);
    }


    @Override
    public ProductResponse reserveProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.isAvailable()) {
            throw new RuntimeException("Product is not available for purchase");
        }

        product.setAvailable(false);
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        return mapToProductResponse(updatedProduct);
    }

    @Override
    public ProductResponse releaseProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setAvailable(true);
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        return mapToProductResponse(updatedProduct);
    }

    @Override
    public List<ProductResponse> searchProducts(String searchTerm) {
        return productRepository.searchProducts(searchTerm).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private Product mapToProduct(ProductRequest productRequest){
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setTags(productRequest.getTags());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setDimensions(mapToProductDimensions(productRequest.getDimensions()));
        product.setMedium(productRequest.getMedium());
        product.setStyle(productRequest.getStyle());
        return product;
    }

    private ProductDimensions mapToProductDimensions(ProductDimensionsDTO dimensionsDTO) {
        if (dimensionsDTO == null) return null;
        ProductDimensions dimensions = new ProductDimensions();
        dimensions.setLength(dimensionsDTO.getLength());
        dimensions.setWidth(dimensionsDTO.getWidth());
        dimensions.setUnit(dimensionsDTO.getUnit());
        return dimensions;
    }

    private void updateProduct(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setTags(productRequest.getTags());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setDimensions(mapToProductDimensions(productRequest.getDimensions()));
        product.setMedium(productRequest.getMedium());
        product.setStyle(productRequest.getStyle());
        product.setUpdatedAt(LocalDateTime.now());
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setArtistId(product.getArtistId());
        response.setCategory(product.getCategory());
        response.setTags(product.getTags());
        response.setImageUrl(product.getImageUrl());
        response.setStockQuantity(product.getStockQuantity());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setAvailable(product.isAvailable());
        response.setDimensions(mapToProductDimensionsDTO(product.getDimensions()));
        response.setMedium(product.getMedium());
        response.setStyle(product.getStyle());
        return response;
    }


    private ProductDimensionsDTO mapToProductDimensionsDTO(ProductDimensions dimensions) {
        if (dimensions == null) return null;
        ProductDimensionsDTO dimensionsDTO = new ProductDimensionsDTO();
        dimensionsDTO.setLength(dimensions.getLength());
        dimensionsDTO.setWidth(dimensions.getWidth());
        dimensionsDTO.setUnit(dimensions.getUnit());
        return dimensionsDTO;
    }
}

