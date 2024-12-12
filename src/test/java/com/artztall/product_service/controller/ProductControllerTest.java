package com.artztall.product_service.controller;
import com.artztall.product_service.dto.*;
import com.artztall.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample data
        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(100.0);
        productRequest.setCategory("Art");
        productRequest.setTags(Arrays.asList("tag1", "tag2"));
        productRequest.setImageUrl("http://example.com/image.jpg");
        productRequest.setStockQuantity(10);
        productRequest.setMedium("Oil");
        productRequest.setStyle("Modern");

        productResponse = new ProductResponse();
        productResponse.setId("product-id");
        productResponse.setName("Test Product");
        productResponse.setDescription("Test Description");
        productResponse.setPrice(100.0);
        productResponse.setCategory("Art");
        productResponse.setTags(Arrays.asList("tag1", "tag2"));
        productResponse.setImageUrl("http://example.com/image.jpg");
        productResponse.setStockQuantity(10);
        productResponse.setAvailable(true);
        productResponse.setMedium("Oil");
        productResponse.setStyle("Modern");
    }

    @Test
    void testCreateProduct() {
        String artistId = "artist-id";
        when(productService.createProduct(any(ProductRequest.class), eq(artistId))).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.createProduct(productRequest, artistId);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
    void testUpdateProduct() {
        String productId = "product-id";
        when(productService.updateProduct(eq(productId), any(ProductRequest.class))).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.updateProduct(productId, productRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
    void testDeleteProduct() {
        ResponseEntity<Void> response = productController.deleteProduct("product-id");

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testGetProductById() {
        String productId = "product-id";
        when(productService.getProductById(productId)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.getProduct(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
    void testGetAllProducts() {
        Page<ProductResponse> page = new PageImpl<>(List.of(productResponse));
        when(productService.getAllProducts(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<ProductResponse>> response = productController.getAllProducts(0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void testGetProductsByArtist() {
        String artistId = "artist-id";
        when(productService.getProductsByArtist(artistId)).thenReturn(List.of(productResponse));

        ResponseEntity<List<ProductResponse>> response = productController.getProductsByArtist(artistId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetProductsByCategory() {
        String category = "Art";
        when(productService.getProductsByCategory(category)).thenReturn(List.of(productResponse));

        ResponseEntity<List<ProductResponse>> response = productController.getProductsByCategory(category);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetProductsByPriceRange() {
        when(productService.getProductsByPriceRange(50.0, 150.0)).thenReturn(List.of(productResponse));

        ResponseEntity<List<ProductResponse>> response = productController.getProductsByPriceRange(50.0, 150.0);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testUpdateProductAvailability() {
        String productId = "product-id";
        AvailabilityUpdateRequest request = new AvailabilityUpdateRequest();
        request.setAvailable(true);
        when(productService.updateProductAvailability(productId, request.isAvailable())).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.updateProductAvailability(productId, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isAvailable());
    }

    @Test
    void testSearchProducts() {
        String query = "Test";
        when(productService.searchProducts(query)).thenReturn(List.of(productResponse));

        ResponseEntity<List<ProductResponse>> response = productController.searchProducts(query);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}

