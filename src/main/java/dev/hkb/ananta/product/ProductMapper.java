package dev.hkb.ananta.product;

import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // resolve these ignored things at the business logic
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    @Mapping(target = "tagSet", ignore = true)
    Product toEntity(CreateProductRequest productRequest);

    @Mapping(source = "id", target = "Id")
    @Mapping(target = "categoryName", expression = "java(product.getCategory().getName())")
    @Mapping(target = "manufacturerName", expression = "java(product.getManufacturer().getBrandName())")
    @Mapping(source = "tagSet", target = "tagNames")
    @Mapping(target = "imageURL", expression = "java(getImageUrl(product))")
    ProductResponse toDto(Product product);

    // for tagNames field to map tagset<TAG> to set<String>
    default Set<String> mapTagsToNames(Set<Tag> tagSet) {
        if (tagSet == null) {
            return null;
        }
        return tagSet.stream()
                .map(Tag::getTag)
                .collect(Collectors.toSet());
    }

//    default String getEncodingImage(Product product){
//        if(product != null &&
//                product.getProductImage() != null){
//            return product.getProductImage().base64Img();
//        }
//        return null;
//    }
//
//    default String getImageType(Product product){
//        if(product != null &&
//                product.getProductImage() != null){
//            return product.getProductImage().imageType();
//        }
//        return null;
//    }

    default String getImageUrl(Product product){
        // Check if the product has an image attached to avoid NullPointerExceptions
        if (product != null && product.getProductImage() != null) {
            // Return the direct Cloudinary URL stored in your database
            return product.getProductImage().getImageUrl();
        }

        // Return null (or a default placeholder image URL) if the product has no image
        return "https://res.cloudinary.com/dfdlovjnz/image/upload/v1776172237/samples/radial_02.png";
    }
}
