package dev.hkb.ananta.sellerProduct;

import dev.hkb.ananta.sellerProduct.dto.CreateSellerProductRequest;
import dev.hkb.ananta.sellerProduct.dto.SellerProductBaseResponse;
import dev.hkb.ananta.sellerProduct.dto.SellerProductFullResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerProductMapper {

    @Mapping(target = "product",ignore = true)
    @Mapping(target = "seller", ignore = true)
    SellerProduct toEntity(CreateSellerProductRequest cspr);

    @Mapping(target = "productId", expression = "java(sellerProduct.getProduct().getId())")
    @Mapping(target = "productName", expression = "java(sellerProduct.getProduct().getName())")
    @Mapping(target = "sellerId", expression = "java(sellerProduct.getSeller().getId())")
    @Mapping(target = "sellerName", expression = "java(sellerProduct.getSeller().getUser().getFirstName())")
    SellerProductBaseResponse toBaseDto(SellerProduct sellerProduct);

    @Mapping(target = "productId", expression = "java(sellerProduct.getProduct().getId())")
    @Mapping(target = "productName", expression = "java(sellerProduct.getProduct().getName())")
    @Mapping(target = "sellerId", expression = "java(sellerProduct.getSeller().getId())")
    @Mapping(target = "sellerName", expression = "java(sellerProduct.getSeller().getUser().getFirstName())")
    @Mapping(target = "productDescription", expression = "java(sellerProduct.getProduct().getDescription())")
    // take these as arguments in method call
    @Mapping(target = "averageRating", source = "avgRating")
    @Mapping(target = "ratings", source = "ratingCount")
    SellerProductFullResponse toFullDto(SellerProduct sellerProduct, Double avgRating, Long ratingCount);
}
