package dev.hkb.ananta.product;

import dev.hkb.ananta.constants.ProductStatus;
import dev.hkb.ananta.image.CloudinaryService;
import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.security.config.SecurityConfig;
import dev.hkb.ananta.security.jwt.JwtUtilService;
import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.sellerProduct.dto.CreateSellerProductRequest;
import dev.hkb.ananta.sellerProduct.dto.SellerProductBaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private JwtUtilService jwtUtilService;
    @MockitoBean
    private CloudinaryService imageService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private JsonMapper jsonMapper;



    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void createProductTest_ShouldReturn200AndProductResponse() throws Exception {

        CreateProductRequest request = new CreateProductRequest("P1", "Good", BigDecimal.valueOf(95),
                4L, 2L, ProductStatus.ACTIVE, Set.of(7L, 14L));
        ProductResponse pr = new ProductResponse(7L, "P1", "Good", BigDecimal.valueOf(95),
                "Tech", "ubu", ProductStatus.ACTIVE, Set.of("Hello"), null,
                OffsetDateTime.now());

        String jsonBody = jsonMapper.writeValueAsString(request);

        when(productService.createProduct(request)).thenReturn(pr);

        mockMvc.perform(post("/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturerName").value("ubu"));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void createProduct_ShouldReturn400_WhenNameIsBlank() throws Exception {
        // 1. ARRANGE: Create a request with an intentionally blank name and null price
        CreateProductRequest invalidRequest = new CreateProductRequest(
                "", // BLANK NAME!
                "Good",
                null, // NULL PRICE!
                4L, 2L, ProductStatus.ACTIVE, Set.of(7L)
        );

        String jsonBody = jsonMapper.writeValueAsString(invalidRequest);

        // 2. ACT & ASSERT
        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))

                // 3. We expect Spring's @Valid to catch this and throw a 400!
                .andExpect(status().isBadRequest());

        // Optional: If your ExceptionHandler returns field errors, you can check them!
        // .andExpect(jsonPath("$.name").value("must not be blank"));
    }

    @Test
    @WithMockUser(username = "username", roles = {"SELLER"})
    void getProductsToSellTest_ShouldReturn200AndListOfProducts() throws Exception {
        ProductResponse pr1 = new ProductResponse(7L, "P1", "Good", BigDecimal.valueOf(95),
                "Tech", "ubu", ProductStatus.ACTIVE, Set.of("Hello"), null,
                OffsetDateTime.now());
        ProductResponse pr2 = new ProductResponse(17L, "P2", "Bad", BigDecimal.valueOf(905),
                "Home", "ubu", ProductStatus.ACTIVE, Set.of("Bye"), null,
                OffsetDateTime.now());
        List<ProductResponse> list = List.of(pr1, pr2);

        when(productService.getProducts(4L, Set.of(9L, 32L))).thenReturn(list);

        mockMvc.perform(get("/products")
                        .param("category", "4")
                        .param("tags", "9", "32")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value("Tech"))
                .andExpect(jsonPath("$[1].categoryName").value("Home"));
    }

    @Test
    void applyToSellTest_ShouldReturn200() throws Exception {
        CreateSellerProductRequest request = new CreateSellerProductRequest(7L,
                BigDecimal.valueOf(7512),7);
        UserPrincipal mockPrincipal = mock(UserPrincipal.class);

        String jsonBody = jsonMapper.writeValueAsString(request);
        String email = "heckapoo@gmail.com";

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_SELLER");

        when(mockPrincipal.getUsername()).thenReturn(email);
        doReturn(List.of(authority)).when(mockPrincipal).getAuthorities();
        doNothing().when(productService).sellerApplyToProduct(request,email);

        mockMvc.perform(post("/products/applications")
                        .with(csrf())
                        .with(user(mockPrincipal))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application submitted successfully"));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void getPendingQueueTest() throws Exception {
        SellerProductBaseResponse pr1 = new SellerProductBaseResponse(1L,"P1",3L,
                24L,"Rathod",BigDecimal.valueOf(950),12,ProductStatus.PENDING,null);
        SellerProductBaseResponse pr2 = new SellerProductBaseResponse(11L,"P2",32L,
                23L,"Vikram",BigDecimal.valueOf(95),2,ProductStatus.PENDING,null);

        List<SellerProductBaseResponse> list = List.of(pr1, pr2);

        when(productService.getPendingApprovals()).thenReturn(list);

        mockMvc.perform(get("/products/pending")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sellerName").value("Rathod"))
                .andExpect(jsonPath("$[1].sellerName").value("Vikram"));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void approvePendingRequestTest_Approved() throws Exception {

        Long listingId = 9L;
        when(productService.approveListing(listingId, true)).thenReturn("Application approved");

        mockMvc.perform(post("/products/approve/{listingId}",listingId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application approved"));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void approvePendingRequestTest_Declined() throws Exception {

        Long listingId = 9L;
        when(productService.approveListing(listingId, false)).thenReturn("Application Rejected");

        mockMvc.perform(post("/products/approve/{listingId}",listingId)
                        .with(csrf())
                        .param("status", "false")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application Rejected"));
    }

}
