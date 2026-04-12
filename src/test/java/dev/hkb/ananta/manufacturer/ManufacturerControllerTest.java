package dev.hkb.ananta.manufacturer;

import dev.hkb.ananta.constants.ProductStatus;
import dev.hkb.ananta.constants.StatusEnum;
import dev.hkb.ananta.exceptionHandler.ManufacturerNotFound;
import dev.hkb.ananta.manufacturer.dto.CreateManufacturerRequest;
import dev.hkb.ananta.manufacturer.dto.ManufacturerResponse;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.security.jwt.JwtUtilService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManufacturerController.class)
public class ManufacturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManufacturerService manufacturerService;
    @MockitoBean
    private JwtUtilService jwtUtilService;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    @WithMockUser
    void getProducts_ShouldReturn200AndJsonList_WhenBrandExist() throws Exception {
        String brandName = "ubu";

        ProductResponse pr1 = new ProductResponse(7L, "pr1", "Good", BigDecimal.valueOf(75),
                "Electronics", "ubu", ProductStatus.ACTIVE, null, null,
                OffsetDateTime.now()
                );
        ProductResponse pr2 = new ProductResponse(17L, "pr2", "Bad", BigDecimal.valueOf(70),
                "General", "ubu", ProductStatus.DISCONTINUED, null, null,
                OffsetDateTime.now()
        );

        List<ProductResponse> products = List.of(pr1, pr2);

        when(manufacturerService.getProducts(brandName)).thenReturn(products);

        mockMvc.perform(get("/manufacturers/{brandName}/products", brandName)
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].categoryName").value("Electronics"))
                .andExpect(jsonPath("$[1].categoryName").value("General"));
    }

    @Test
    @WithMockUser
    void getProducts_WhenBrandDoesNotExist() throws Exception {
        String brandName = "ubu";

        when(manufacturerService.getProducts(brandName))
                .thenThrow(new ManufacturerNotFound("Manufacturer Not Found"));

        mockMvc.perform(get("/manufacturers/{brandName}/products", brandName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void createManufactureTest() throws Exception {

        CreateManufacturerRequest request = new CreateManufacturerRequest("ubu","29fb3sj4bf34", StatusEnum.ACTIVE);
        ManufacturerResponse response = new ManufacturerResponse(4L,"ubu","29fb3sj4bf34", StatusEnum.ACTIVE);

        String jsonBody = jsonMapper.writeValueAsString(request);

        when(manufacturerService.addManufacturer(request)).thenReturn(response);

        mockMvc.perform(post("/manufacturers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))

                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void getManufacturersTest_ShouldReturn200() throws Exception {

        ManufacturerResponse m1 = new ManufacturerResponse(4L,"ubu","29fb3sj4bf34", StatusEnum.ACTIVE);
        ManufacturerResponse m2 = new ManufacturerResponse(7L,"guggu","viebs3efksvid", StatusEnum.ACTIVE);

        List<ManufacturerResponse> mockList = List.of(m1, m2);

        when(manufacturerService.getAllManufacturers()).thenReturn(mockList);

        mockMvc.perform(get("/manufacturers")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brandName").value("ubu"))
                .andExpect(jsonPath("$[1].brandName").value("guggu"));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void deleteManufacturerTest() throws Exception {
        Long manufacturerId = 7L;

        doNothing().when(manufacturerService).deleteManufacturer(manufacturerId);

        mockMvc.perform(delete("/manufacturers/{manufacturerId}", manufacturerId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());

        verify(manufacturerService).deleteManufacturer(manufacturerId);
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void deleteManufacturer_ManufacturerNotFoundTest() throws Exception {
        Long manufacturerId = 7L;

        doThrow(new ManufacturerNotFound("Manufacturer Not Found")).when(manufacturerService).deleteManufacturer(manufacturerId);

        mockMvc.perform(delete("/manufacturers/{manufacturerId}", manufacturerId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        verify(manufacturerService).deleteManufacturer(manufacturerId);
    }


}
