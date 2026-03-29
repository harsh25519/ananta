package dev.hkb.ananta.product;

import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

        String imgUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/products/")
                .path(product.getId().toString())
                .path("/images")
                .toUriString();
        return imgUrl;
    }
}
