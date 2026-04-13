package dev.hkb.ananta.product;

import dev.hkb.ananta.category.Category;
import dev.hkb.ananta.category.CategoryRepository;
import dev.hkb.ananta.constants.*;
import dev.hkb.ananta.manufacturer.Manufacturer;
import dev.hkb.ananta.manufacturer.ManufacturerRepository;
import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.seller.Seller;
import dev.hkb.ananta.seller.SellerRepository;
import dev.hkb.ananta.sellerProduct.SellerProduct;
import dev.hkb.ananta.sellerProduct.SellerProductRepository;
import dev.hkb.ananta.sellerProduct.dto.CreateSellerProductRequest;
import dev.hkb.ananta.tag.Tag;
import dev.hkb.ananta.tag.TagRepository;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerProductRepository sellerProductRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateAndRetrieveProduct() throws Exception {
        Manufacturer m1 = new Manufacturer("Intel","2jd9anbfk4nf9s", StatusEnum.ACTIVE);
        m1 = manufacturerRepository.saveAndFlush(m1);
        Category c1 = new Category("Tech");
        c1 = categoryRepository.saveAndFlush(c1);
        Tag t1 = new Tag("Tech");
        t1 = tagRepository.saveAndFlush(t1);

        CreateProductRequest request = new CreateProductRequest("CPU", "High processing",
                BigDecimal.valueOf(77777),c1.getId(), m1.getId(), ProductStatus.ACTIVE, Set.of(t1.getId()));

        String jsonBody = jsonMapper.writeValueAsString(request);
        mockMvc.perform(post("/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CPU"));

        List<Product> allProducts = productRepository.findAll();

        assertThat(allProducts).isNotNull();
        assertThat(allProducts).hasSize(1);
        assertThat(allProducts.get(0).getName()).isEqualTo("CPU");
    }

    @Test
    @WithMockUser(roles = {"SELLER"})
    void getProductsToSellTest() throws Exception {
        Manufacturer m1 = new Manufacturer("Intel","2jd9anbfk4nf9s",StatusEnum.ACTIVE);
        Manufacturer m2 = new Manufacturer("Lava", "2rw4df46tg42",StatusEnum.ACTIVE);
        m1 = manufacturerRepository.saveAndFlush(m1);
        m2 = manufacturerRepository.saveAndFlush(m2);
        Category c1 = new Category("General");
        c1 = categoryRepository.saveAndFlush(c1);

        Product p1 = new Product("Prod1", BigDecimal.valueOf(94),"Good product", c1,
                m1, ProductStatus.ACTIVE);
        Product p2 = new Product("Prod2", BigDecimal.valueOf(99),"Bad product", c1,
                m2, ProductStatus.ACTIVE);
        Product p3 = new Product("Prod3", BigDecimal.valueOf(777),"Bad product", c1,
                m2, ProductStatus.ACTIVE);
        productRepository.saveAllAndFlush(List.of(p1,p2,p3));

        Tag t1 = new Tag("Tech");
        t1 = tagRepository.saveAndFlush(t1);
        Long catId = c1.getId();
        Long tagId = t1.getId();

        mockMvc.perform(get("/products")
                        .param("category", String.valueOf(catId))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));
    }

    @Test
    void sellerApplyToSellProductsTest() throws Exception {
        Users u1 = new Users("Haggu", "Malgudi","malgudi@gmail.com",
                "hashedPass","9581265475", UserRoles.SELLER);
        u1 = userRepository.save(u1);
        Seller seller = new Seller(u1, "abc","345tfgder4es","xyzgtf","ertyu",
                StateEnum.HP,"751235", CountryEnum.IN);
        sellerRepository.saveAndFlush(seller);

        UserPrincipal fakePrincipal = new UserPrincipal(u1);

        Manufacturer m1 = new Manufacturer("Intel","2jd9anbfk4nf9s",StatusEnum.ACTIVE);
        Category c1 = new Category("General");
        m1 = manufacturerRepository.saveAndFlush(m1);
        c1 = categoryRepository.saveAndFlush(c1);

        Product p1 = new Product("Prod1", BigDecimal.valueOf(94),"Good product", c1,
                m1, ProductStatus.ACTIVE);
        p1 = productRepository.saveAndFlush(p1);

        CreateSellerProductRequest request = new CreateSellerProductRequest(p1.getId(),BigDecimal.valueOf(70),12);
        String jsonBody = jsonMapper.writeValueAsString(request);

        mockMvc.perform(post("/products/applications")
                .with(csrf())
                .with(user(fakePrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application submitted successfully"));

        List<SellerProduct> s1 = sellerProductRepository.findAll();
        assertThat(s1).hasSize(1);
        assertThat(s1.get(0).getQuantity()).isEqualTo(12);
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"}) // Customers should NOT be able to create global products
    void customerShouldNotBeAbleToCreateProduct() throws Exception {
        CreateProductRequest request = new CreateProductRequest("CPU", "High processing",
                BigDecimal.valueOf(77777),14L, 12L, ProductStatus.ACTIVE, Set.of(4L));
        String jsonBody = jsonMapper.writeValueAsString(request);

        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden());
    }
}
