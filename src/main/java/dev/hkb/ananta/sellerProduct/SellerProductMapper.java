package dev.hkb.ananta.sellerProduct;

import dev.hkb.ananta.sellerProduct.dto.CreateSellerProductRequest;
import dev.hkb.ananta.sellerProduct.dto.SellerProductBaseResponse;
import dev.hkb.ananta.sellerProduct.dto.SellerProductFullResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Mapper(componentModel = "spring")
public interface SellerProductMapper {

    @Mapping(target = "product",ignore = true)
    @Mapping(target = "seller", ignore = true)
    SellerProduct toEntity(CreateSellerProductRequest cspr);

    @Mapping(target = "productId", expression = "java(sellerProduct.getProduct().getId())")
    @Mapping(target = "productName", expression = "java(sellerProduct.getProduct().getName())")
    @Mapping(target = "sellerProductId", source = "id")
    @Mapping(target = "sellerId", expression = "java(sellerProduct.getSeller().getId())")
    @Mapping(target = "sellerName", expression = "java(sellerProduct.getSeller().getUser().getFirstName())")
    @Mapping(target = "status", source = "productStatus")
    @Mapping(target = "imageURL", expression = "java(getImageUrl(sellerProduct))")
    SellerProductBaseResponse toBaseDto(SellerProduct sellerProduct);

    @Mapping(target = "productId", expression = "java(sellerProduct.getProduct().getId())")
    @Mapping(target = "productName", expression = "java(sellerProduct.getProduct().getName())")
    @Mapping(target = "sellerId", expression = "java(sellerProduct.getSeller().getId())")
    @Mapping(target = "sellerName", expression = "java(sellerProduct.getSeller().getUser().getFirstName())")
    @Mapping(target = "productDescription", expression = "java(sellerProduct.getProduct().getDescription())")
    @Mapping(source = "sellerProduct.productStatus", target = "status")
    @Mapping(target = "imageURL", expression = "java(getImageUrl(sellerProduct))")
    // take these as arguments in method call
    @Mapping(target = "averageRating", source = "avgRating")
    @Mapping(target = "ratings", source = "ratingCount")
    SellerProductFullResponse toFullDto(SellerProduct sellerProduct, Double avgRating, Long ratingCount);


//    default String getEncodingImage(SellerProduct sellerProduct){
//        if(sellerProduct != null && sellerProduct.getProduct() != null &&
//                sellerProduct.getProduct().getProductImage() != null){
//            return sellerProduct.getProduct().getProductImage().base64Img();
//        }
//        return null;
//    }
//
//    default String getImageType(SellerProduct sellerProduct){
//        if(sellerProduct != null && sellerProduct.getProduct() != null &&
//                sellerProduct.getProduct().getProductImage() != null){
//            return sellerProduct.getProduct().getProductImage().imageType();
//        }
//        return null;
//    }

    default String getImageUrl(SellerProduct sellerProduct){

        String imgUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/products/")
                    .path(sellerProduct.getProduct().getId().toString())
                    .path("/images")
                    .toUriString();
        return imgUrl;
    }

}
