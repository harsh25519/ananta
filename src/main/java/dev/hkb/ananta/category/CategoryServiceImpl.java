package dev.hkb.ananta.category;

import dev.hkb.ananta.category.dto.CategoryResponse;
import dev.hkb.ananta.category.dto.CreateCategoryRequest;
import dev.hkb.ananta.exceptionHandler.CategoryNotFound;
import dev.hkb.ananta.product.ProductMapper;
import dev.hkb.ananta.product.ProductRepository;
import dev.hkb.ananta.product.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper,
                               ProductMapper productMapper, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public CategoryResponse addCategory(CreateCategoryRequest csr) {
        Category category = categoryMapper.toEntity(csr);

        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public List<ProductResponse> getProducts(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFound("Category not found"));

        return productRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFound("Category does not exist"));

        if (category.getName().equalsIgnoreCase("General")) {
            throw new RuntimeException("The General category is a system default and cannot be deleted.");
        }

        Category generalCategory = categoryRepository.findByName("General")
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName("General");
                    return categoryRepository.save(c);
                });

        productRepository.updateCategoryForProducts(categoryId, generalCategory);

        categoryRepository.delete(category);
    }
}
