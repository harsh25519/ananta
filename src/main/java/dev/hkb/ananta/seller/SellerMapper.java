package dev.hkb.ananta.seller;

import dev.hkb.ananta.seller.dto.CreateSellerRequest;
import dev.hkb.ananta.seller.dto.SellerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    Seller toEntity(CreateSellerRequest sellerDto);

    @Mapping(target = "userName",
            expression = "java(seller.getUser().getFirstName() + \"\" + seller.getUser().getLastName())")
    @Mapping(target = "email",
            expression = "java(seller.getUser().getEmail())")
    @Mapping(target = "userCreatedAt",
            expression = "java(seller.getUser().getCreatedAt())")
    @Mapping(source = "user.role", target = "role")
    SellerResponse toDto(Seller seller);
}
