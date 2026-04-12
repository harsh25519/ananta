package dev.hkb.ananta.product;

import dev.hkb.ananta.category.Category;
import dev.hkb.ananta.category.CategoryRepository;
import dev.hkb.ananta.constants.ProductStatus;
import dev.hkb.ananta.constants.StatusEnum;
import dev.hkb.ananta.manufacturer.Manufacturer;
import dev.hkb.ananta.manufacturer.ManufacturerRepository;
import dev.hkb.ananta.tag.Tag;
import dev.hkb.ananta.tag.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TestEntityManager entityManager;

    private Manufacturer m1;
    private Category c1;
    private Category c2;
    private Product p1;
    private Product p2;
    private Product p3;

    @BeforeEach
    void setUp(){
        m1 = new Manufacturer("ubu","j39bf34ksnf", StatusEnum.ACTIVE);
        manufacturerRepository.saveAndFlush(m1);

        c1 = new Category("Elec");
        categoryRepository.saveAndFlush(c1);
        c2 = new Category("Grocery");
        categoryRepository.saveAndFlush(c2);

        p1 = new Product("P1", BigDecimal.valueOf(57), "Good product", c1, m1, ProductStatus.ACTIVE);
        p2 = new Product("P2", BigDecimal.valueOf(57), "Good product", c1, m1, ProductStatus.ACTIVE);
        p3 = new Product("P3", BigDecimal.valueOf(57), "Good product", c2, m1, ProductStatus.ACTIVE);
    }

    @Test
    void findAllByManufacturerId_ShouldReturnProducts(){

        productRepository.saveAndFlush(p1);
        productRepository.saveAndFlush(p2);
        productRepository.saveAndFlush(p3);

        List<Product> products = productRepository.findAllByManufacturerId(m1.getId());

        assertThat(products).isNotNull();
        assertThat(products).hasSize(3);
        assertThat(products.get(0).getManufacturer().getId()).isEqualTo(m1.getId());
    }

    @Test
    void findAllByCategoryId_ShouldReturnProducts(){

        productRepository.saveAndFlush(p1);
        productRepository.saveAndFlush(p2);
        productRepository.saveAndFlush(p3);

        List<Product> products = productRepository.findAllByCategoryId(c1.getId());

        assertThat(products).isNotNull();
        assertThat(products).hasSize(2);
        assertThat(products.get(0).getName()).isEqualTo("P1");
        assertThat(products.get(1).getName()).isEqualTo("P2");
    }

    @Test
    void findByCategoryOrTags_ShouldReturnTags(){
        Tag t1 = new Tag("Tech");
        Tag t2 = new Tag("House");
        tagRepository.saveAndFlush(t1);
        tagRepository.saveAndFlush(t2);

        p1.setTagSet(Set.of(t1));
        p2.setTagSet(Set.of(t1, t2));
        p3.setTagSet(Set.of(t2));

        productRepository.saveAndFlush(p1);
        productRepository.saveAndFlush(p2);
        productRepository.saveAndFlush(p3);

        List<Product> products = productRepository.findByCategoryOrTags(c1.getId(), Set.of(t2.getId()));

        assertThat(products).isNotNull();
        assertThat(products).hasSize(3);
//        assertThat(products.get(0).getName()).isEqualTo("P1");
//        assertThat(products.get(1).getName()).isEqualTo("P2");
    }

    @Test
    void updateCategoryForProduct_ReturnVoid(){
        Category oldCat = new Category("Tech");
        categoryRepository.saveAndFlush(oldCat);
        Category newCat = new Category("Electronic");
        categoryRepository.saveAndFlush(newCat);

        Product mockProduct = new Product("MOCK", BigDecimal.valueOf(57), "Good product", oldCat, m1, ProductStatus.ACTIVE);
        productRepository.saveAndFlush(mockProduct);

        productRepository.updateCategoryForProducts(oldCat.getId(), newCat);

        // clear cache
        entityManager.clear();

        Product updatedProduct = productRepository.findById(mockProduct.getId()).get();

        assertThat(updatedProduct.getCategory().getId()).isEqualTo(newCat.getId());
        assertThat(updatedProduct.getCategory().getName()).isEqualTo("Electronic");
    }

    @Test
    void findAllByTagSet_IdTest(){
        Tag t1 = new Tag("Tech");
        Tag t2 = new Tag("House");
        tagRepository.saveAndFlush(t1);
        tagRepository.saveAndFlush(t2);

        p1.setTagSet(Set.of(t1));
        p2.setTagSet(Set.of(t1, t2));
        p3.setTagSet(Set.of(t2));

        productRepository.saveAndFlush(p1);
        productRepository.saveAndFlush(p2);
        productRepository.saveAndFlush(p3);

        List<Product> products = productRepository.findAllByTagSet_Id(t1.getId());

        assertThat(products).isNotNull();
        assertThat(products).hasSize(2);
    }
}
