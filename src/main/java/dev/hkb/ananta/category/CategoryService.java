package dev.hkb.ananta.category;

import dev.hkb.ananta.category.dto.CategoryResponse;
import dev.hkb.ananta.category.dto.CreateCategoryRequest;
import dev.hkb.ananta.product.dto.ProductResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse addCategory(CreateCategoryRequest csr);

    List<CategoryResponse> getAllCategories();

    List<ProductResponse> getProducts(Long categoryId);

    void deleteCategory(Long categoryId);

}
