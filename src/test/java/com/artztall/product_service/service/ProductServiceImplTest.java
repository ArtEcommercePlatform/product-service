package com.artztall.product_service.service;

import com.artztall.product_service.dto.ProductDimensionsDTO;
import com.artztall.product_service.dto.ProductRequest;
import com.artztall.product_service.dto.ProductResponse;
import com.artztall.product_service.model.Product;
import com.artztall.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductRequest request = createProductRequest();
        Product product = createProduct();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(request, "artist123");

        assertNotNull(response);
        assertEquals("Product 1", response.getName());
        assertEquals("artist123", response.getArtistId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        ProductRequest request = createProductRequest();
        Product existingProduct = createProduct();
        when(productRepository.findById("1")).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ProductResponse response = productService.updateProduct("1", request);

        assertNotNull(response);
        assertEquals("Product 1", response.getName());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById("1");

        productService.deleteProduct("1");

        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    void testGetProductById() {
        Product product = createProduct();
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById("1");

        assertNotNull(response);
        assertEquals("Product 1", response.getName());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void testGetAllProducts() {
        Product product = createProduct();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<ProductResponse> responsePage = productService.getAllProducts(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getContent().size());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetProductsByArtist() {
        Product product = createProduct();
        when(productRepository.findByArtistId("artist123")).thenReturn(List.of(product));

        List<ProductResponse> responses = productService.getProductsByArtist("artist123");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Product 1", responses.get(0).getName());
        verify(productRepository, times(1)).findByArtistId("artist123");
    }

    @Test
    void testGetProductsByCategory() {
        Product product = createProduct();
        when(productRepository.findByCategory("Category 1")).thenReturn(List.of(product));

        List<ProductResponse> responses = productService.getProductsByCategory("Category 1");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Product 1", responses.get(0).getName());
        verify(productRepository, times(1)).findByCategory("Category 1");
    }

    @Test
    void testUpdateProductAvailability() {
        Product product = createProduct();
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.updateProductAvailability("1", false);

        assertNotNull(response);
        assertFalse(response.isAvailable());
        verify(productRepository, times(1)).save(product);
    }

    private Product createProduct() {
        Product product = new Product();
        product.setId("1");
        product.setName("Product 1");
        product.setDescription("Description");
        product.setPrice(100.0);
        product.setArtistId("artist123");
        product.setCategory("Category 1");
        product.setTags(Arrays.asList("tag1", "tag2"));
        product.setImageUrl("http://example.com/image.jpg");
        product.setStockQuantity(10);
        product.setAvailable(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }

    private ProductRequest createProductRequest() {
        ProductRequest request = new ProductRequest();
        request.setName("Product 1");
        request.setDescription("Description");
        request.setPrice(100.0);
        request.setCategory("Category 1");
        request.setTags(Arrays.asList("tag1", "tag2"));
        request.setImageUrl("http://example.com/image.jpg");
        request.setStockQuantity(10);
        ProductDimensionsDTO dimensions = new ProductDimensionsDTO();
        dimensions.setLength(10.0);
        dimensions.setWidth(5.0);
        dimensions.setUnit("cm");
        request.setDimensions(dimensions);
        request.setMedium("Oil");
        request.setStyle("Abstract");
        return request;
    }
}
