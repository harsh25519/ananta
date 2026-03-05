package dev.hkb.ananta.mapper;

import dev.hkb.ananta.dto.seller.CreateSellerRequest;
import dev.hkb.ananta.dto.seller.SellerResponse;
import dev.hkb.ananta.entity.Seller;
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
