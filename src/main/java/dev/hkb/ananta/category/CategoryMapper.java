package dev.hkb.ananta.category;

import dev.hkb.ananta.category.dto.CategoryResponse;
import dev.hkb.ananta.category.dto.CreateCategoryRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CreateCategoryRequest request);

    CategoryResponse toDto(Category category);
}