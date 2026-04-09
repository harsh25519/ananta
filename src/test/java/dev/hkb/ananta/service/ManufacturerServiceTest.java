package dev.hkb.ananta.service;

import dev.hkb.ananta.category.Category;
import dev.hkb.ananta.constants.ProductStatus;
import dev.hkb.ananta.constants.StatusEnum;
import dev.hkb.ananta.exceptionHandler.ManufacturerNotFound;
import dev.hkb.ananta.manufacturer.Manufacturer;
import dev.hkb.ananta.manufacturer.ManufacturerMapper;
import dev.hkb.ananta.manufacturer.ManufacturerRepository;
import dev.hkb.ananta.manufacturer.ManufacturerServiceImpl;
import dev.hkb.ananta.manufacturer.dto.CreateManufacturerRequest;
import dev.hkb.ananta.manufacturer.dto.ManufacturerResponse;
import dev.hkb.ananta.product.Product;
import dev.hkb.ananta.product.ProductMapper;
import dev.hkb.ananta.product.ProductRepository;
import dev.hkb.ananta.product.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManufacturerServiceTest {

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private ManufacturerMapper manufacturerMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    @Test
    void addManufacturerTest(){
        CreateManufacturerRequest request = new CreateManufacturerRequest("Testing","794613thfk54",
                StatusEnum.ACTIVE);
        Manufacturer mockManufacturer = new Manufacturer();
        mockManufacturer.setBrandName(request.brandName());
        mockManufacturer.setLicenseKey(request.licenseKey());

        when(manufacturerMapper.toEntity(any())).thenReturn(mockManufacturer);
        when(manufacturerRepository.save(any())).thenReturn(mockManufacturer);
        when(manufacturerMapper.toDto(any())).thenReturn(new ManufacturerResponse(2L, "Testing",
                "794613thfk54", StatusEnum.ACTIVE));

        ManufacturerResponse mr = manufacturerService.addManufacturer(request);

        assertNotNull(mr);
        assertEquals("Testing", mr.brandName());

        verify(manufacturerRepository).save(mockManufacturer);
        verify(manufacturerMapper).toEntity(request);
        verify(manufacturerMapper).toDto(mockManufacturer);
    }

    @Test
    void getAllManufacturersTest(){
        Manufacturer m1 = new Manufacturer();
        m1.setLicenseKey("94sfgr4e6v6e1");

        Manufacturer m2 = new Manufacturer();
        m2.setLicenseKey("8vsgesxg1645");

        List<Manufacturer> mockManufacturers = List.of(m1, m2);

        ManufacturerResponse dto1 = new ManufacturerResponse(2L,"uib","94sfgr4e6v6e1",StatusEnum.ACTIVE);
        ManufacturerResponse dto2 = new ManufacturerResponse(7L,"aib","8vsgesxg1645",StatusEnum.ACTIVE);

        when(manufacturerRepository.findAll()).thenReturn(mockManufacturers);
        when(manufacturerMapper.toDto(m1)).thenReturn(dto1);
        when(manufacturerMapper.toDto(m2)).thenReturn(dto2);

        List<ManufacturerResponse> mockResult = manufacturerService.getAllManufacturers();

        assertNotNull(mockResult);
        assertEquals("94sfgr4e6v6e1",mockResult.get(0).licenseKey());

        verify(manufacturerRepository).findAll();
        verify(manufacturerMapper,times(2)).toDto(any());
    }


    @Test
    void getProducts(){
        Manufacturer m1 = new Manufacturer();
        m1.setId(4L);
        m1.setLicenseKey("94sfgr4e6v6e1");
        m1.setBrandName("ubu");

        Category mockCat = new Category("Hello");
        Product product = new Product("brand",BigDecimal.valueOf(8461),"Good", mockCat, m1, ProductStatus.ACTIVE);
        List<Product> products = List.of(product);

        ProductResponse pr = new ProductResponse(1L, "brand","Good", BigDecimal.valueOf(8461),
                mockCat.getName(), m1.getBrandName(), ProductStatus.ACTIVE, null, null, OffsetDateTime.now());

        when(manufacturerRepository.getManufacturerByBrandName(any())).thenReturn(Optional.of(m1));
        when(productRepository.findAllByManufacturerId(any())).thenReturn(products);
        when(productMapper.toDto(any())).thenReturn(pr);

        List<ProductResponse> list = manufacturerService.getProducts("ubu");

        assertNotNull(list);
        assertEquals("Hello",list.get(0).categoryName());

        verify(manufacturerRepository).getManufacturerByBrandName(any());
        verify(productRepository).findAllByManufacturerId(any());
        verify(productMapper, times(1)).toDto(any());
    }

    @Test
    void getProducts_ManufacturerNotFound(){
        String brandName = "ubu";

        when(manufacturerRepository.getManufacturerByBrandName(brandName)).thenReturn(Optional.empty());

        assertThrows(ManufacturerNotFound.class, () -> {
            manufacturerService.getProducts(brandName);
        });

        verify(manufacturerRepository).getManufacturerByBrandName(brandName);
        verify(productRepository, never()).findAllByManufacturerId(any());
    }

    @Test
    void deleteManufacturerTest(){
        Long mId = 4L;
        String brandName = "ubu";

        Manufacturer m1 = new Manufacturer();
        m1.setId(4L);
        m1.setLicenseKey("94sfgr4e6v6e1");
        m1.setBrandName("ubu");

        when(manufacturerRepository.findById(mId)).thenReturn(Optional.of(m1));

        manufacturerService.deleteManufacturer(mId);

        assertEquals(StatusEnum.INACTIVE, m1.getStatus());

        verify(manufacturerRepository).findById(mId);
        verify(manufacturerRepository).save(m1);
    }

    @Test
    void deleteManufacturer_NotFoundTest(){
        Long mId = 4L;
        String brandName = "ubu";

        when(manufacturerRepository.findById(mId)).thenReturn(Optional.empty());

        assertThrows(ManufacturerNotFound.class, () -> {
            manufacturerService.deleteManufacturer(mId);
        });

        verify(manufacturerRepository, never()).save(any());
    }

}
