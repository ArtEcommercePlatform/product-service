package com.artztall.product_service.controller;

import com.artztall.product_service.dto.AvailabilityUpdateRequest;
import com.artztall.product_service.dto.ProductRequest;
import com.artztall.product_service.dto.ProductResponse;
import com.artztall.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management API")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product for an artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest productRequest,
            @RequestHeader("Artist-ID") String artistId) {
        ProductResponse response = productService.createProduct(productRequest, artistId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates an existing product")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @RequestBody ProductRequest productRequest) {
        ProductResponse response = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes an existing product")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a product by its ID")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products with pagination")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductResponse> response = productService.getAllProducts(PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/artist/{artistId}")
    @Operation(summary = "Get products by artist", description = "Retrieves all products for a specific artist")
    public ResponseEntity<List<ProductResponse>> getProductsByArtist(@PathVariable String artistId) {
        List<ProductResponse> response = productService.getProductsByArtist(artistId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieves all products in a specific category")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponse> response = productService.getProductsByCategory(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Get products by price range", description = "Retrieves all products within a price range")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<ProductResponse> response = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update product availability", description = "Update Product availability.")
    public ResponseEntity<ProductResponse> updateProductAvailability(
            @PathVariable String id,
            @RequestBody AvailabilityUpdateRequest request) {
        ProductResponse updatedProduct = productService.updateProductAvailability(id, request.isAvailable());
        return ResponseEntity.ok(updatedProduct);
    }

    @PutMapping("/{id}/reserve")
    @Operation(summary = "Reserve Product", description = "Reserve Product.")
    public ResponseEntity<ProductResponse> reserveProduct(@PathVariable String id) {
        try {
            ProductResponse response = productService.reserveProduct(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/release")
    @Operation(summary = "Release product ", description = "Release Product.")
    public ResponseEntity<ProductResponse> releaseProduct(@PathVariable String id) {
        try {
            ProductResponse response = productService.releaseProduct(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
