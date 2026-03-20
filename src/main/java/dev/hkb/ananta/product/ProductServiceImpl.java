package dev.hkb.ananta.product;

import dev.hkb.ananta.category.Category;
import dev.hkb.ananta.category.CategoryRepository;
import dev.hkb.ananta.manufacturer.Manufacturer;
import dev.hkb.ananta.manufacturer.ManufacturerRepository;
import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.tag.Tag;
import dev.hkb.ananta.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService{

    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final TagRepository tagRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
                              ManufacturerRepository manufacturerRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    @Override
    public ProductResponse createProduct(CreateProductRequest cpr) {
        Product product = productMapper.toEntity(cpr);

        // resolving fields that we intentionally ignored at mapper
        Manufacturer manufacturer = manufacturerRepository.findById(cpr.manufacturerId())
                .orElseThrow(() -> new RuntimeException("Manufacturer does not exist"));

        Category category = categoryRepository.findById(cpr.categoryId())
                .orElseThrow(() -> new RuntimeException("Category does not exist"));

        Set<Tag> tags = findTagById(cpr.tagIds());

        // setting those fields to product to save in DB
        product.setManufacturer(manufacturer);
        product.setCategory(category);
        product.setTagSet(tags);

        productRepository.save(product);

        return productMapper.toDto(product);
    }

    // function to get tag set using tag ids
    private Set<Tag> findTagById(Set<Long> tagIds){
        Set<Tag> tags= new HashSet<>();
        for(Long id : tagIds){
            Tag tag = tagRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tag does not exist"));
            tags.add(tag);
        }
        return tags;
    }
}
