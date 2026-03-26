package dev.hkb.ananta.review;

import dev.hkb.ananta.review.dto.CreateReviewRequest;
import dev.hkb.ananta.review.dto.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review toEntity(CreateReviewRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "customerId", source = "user.id")
    @Mapping(target = "customerName", expression = "java(review.getUser().getFirstName() + \"\" + review.getUser().getLastName())")
    ReviewResponse toDto(Review review);
}
