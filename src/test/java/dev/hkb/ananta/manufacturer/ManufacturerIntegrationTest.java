package dev.hkb.ananta.manufacturer;

import dev.hkb.ananta.category.Category;
import dev.hkb.ananta.category.CategoryRepository;
import dev.hkb.ananta.constants.ProductStatus;
import dev.hkb.ananta.constants.StatusEnum;
import dev.hkb.ananta.manufacturer.dto.CreateManufacturerRequest;
import dev.hkb.ananta.product.Product;
import dev.hkb.ananta.product.ProductRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ManufacturerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateAndRetrieveManufacturer() throws Exception {

        // 1. ARRANGE: Create the Request Object
        CreateManufacturerRequest request = new CreateManufacturerRequest(
                "Intel",
                "4j5hf9sn3ksn4",
                StatusEnum.ACTIVE
        );
        String jsonBody = jsonMapper.writeValueAsString(request);

        // 2. ACT: Send the request to the REAL controller
        mockMvc.perform(post("/manufacturers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))

                // 3. ASSERT (Web Layer): Check the response status and JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brandName").value("Intel"));

        // 4. ASSERT (Database Layer): Verify it's REALLY in the H2 Database
        // This is the "Integration" part—checking if the repository actually worked
        List<Manufacturer> allManufacturers = manufacturerRepository.findAll();

        assertThat(allManufacturers).isNotNull();
        assertThat(allManufacturers).hasSize(1);
        assertThat(allManufacturers.get(0).getBrandName()).isEqualTo("Intel");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetAllManufacturers() throws Exception {
        Manufacturer m1 = new Manufacturer("Intel","2jd9anbfk4nf9s",StatusEnum.ACTIVE);
        Manufacturer m2 = new Manufacturer("Lava", "2rw4df46tg42",StatusEnum.DISCONTINUED);
        manufacturerRepository.save(m1);
        manufacturerRepository.save(m2);

        mockMvc.perform(get("/manufacturers")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brandName").value(m1.getBrandName()))
                .andExpect(jsonPath("$[1].licenseKey").value(m2.getLicenseKey()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetProductsByManufacturerName() throws Exception {
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

        mockMvc.perform(get("/manufacturers/{brandName}/products",m2.getBrandName())
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Prod2"))
                .andExpect(jsonPath("$[1].name").value("Prod3"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteManufacturerTestShouldReturn200() throws Exception {
        Manufacturer m1 = new Manufacturer("Intel","2jd9anbfk4nf9s",StatusEnum.ACTIVE);
        m1 = manufacturerRepository.save(m1);

        mockMvc.perform(delete("/manufacturers/{mId}",m1.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().string("Manufacturer Deleted"));

        var exists = manufacturerRepository.findById(m1.getId()).orElseThrow();;
        assertThat(exists.getStatus()).isEqualTo(StatusEnum.INACTIVE);
    }
}
