package dev.hkb.ananta.category;

import dev.hkb.ananta.category.dto.CategoryResponse;
import dev.hkb.ananta.category.dto.CreateCategoryRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /// Category first created by Admin then only those categories can be applied to product
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryRequest csr){
        CategoryResponse categoryResponse = categoryService.addCategory(csr);

        return ResponseEntity.ok(categoryResponse);
    }

    /// Anyone can access category list
    @GetMapping
    @SecurityRequirements()
    public ResponseEntity<?> getCategoryList(){
        List<CategoryResponse> list = categoryService.getAllCategories();

        return ResponseEntity.ok(list);
    }

    /// Anyone can access products just by categories
    @GetMapping("/{category_id}/products")
    @SecurityRequirements()
    public ResponseEntity<?> getProducts(@PathVariable Long category_id){
        List<ProductResponse> products = categoryService.getProducts(category_id);
        return ResponseEntity.ok(products);
    }

    /// Delete Category (ADMIN ONLY)
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category Deleted");
    }
}
