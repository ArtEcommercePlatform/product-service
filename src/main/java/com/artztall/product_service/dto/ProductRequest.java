package com.artztall.product_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.dynalink.linker.LinkerServices;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "DTO representing the artworks details.")
public class ProductRequest {
    @Schema(description = "name of the art")
    private String name;

    @Schema(description = "description about art.")
    private String description;

    @Schema(description = "price of the art")
    private double price;

    @Schema(description = "category of the art")
    private String category;

    @Schema(description = "List of tags related to art")
    private List<String> tags;

    @Schema(description = "image url of the art")
    private String imageUrl;

    @Schema(description = "available quantity of the art")
    private Integer stockQuantity;

    @Schema(description = "dimensions of the art")
    private ProductDimensionsDTO dimensions;

    @Schema(description = "Medium of the art")
    private String medium;

    @Schema(description = "style of the art")
    private String style;

}
