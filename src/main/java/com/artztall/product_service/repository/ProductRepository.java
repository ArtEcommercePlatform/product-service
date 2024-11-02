package com.artztall.product_service.repository;

import com.artztall.product_service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByArtistId(String artistId);
    List<Product> findByCategory(String category);

    List<Product> findByTagsContaining(String tag);

    @Query("{'price': {$gte: ?0, $lte: ?1}}")
    List<Product> findByPriceRange(Double minPrice, Double maxPrice);
}
