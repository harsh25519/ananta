package dev.hkb.ananta.product;

import dev.hkb.ananta.category.Category;
import dev.hkb.ananta.category.CategoryRepository;
import dev.hkb.ananta.constants.ProductStatus;
import dev.hkb.ananta.constants.StatusEnum;
import dev.hkb.ananta.exceptionHandler.*;
import dev.hkb.ananta.manufacturer.Manufacturer;
import dev.hkb.ananta.manufacturer.ManufacturerRepository;
import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.seller.Seller;
import dev.hkb.ananta.seller.SellerRepository;
import dev.hkb.ananta.sellerProduct.SellerProduct;
import dev.hkb.ananta.sellerProduct.SellerProductMapper;
import dev.hkb.ananta.sellerProduct.SellerProductRepository;
import dev.hkb.ananta.sellerProduct.dto.CreateSellerProductRequest;
import dev.hkb.ananta.sellerProduct.dto.SellerProductBaseResponse;
import dev.hkb.ananta.tag.Tag;
import dev.hkb.ananta.tag.TagRepository;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private SellerProductRepository sellerProductRepository;
    @Mock
    private SellerProductMapper sellerProductMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SellerRepository sellerRepository;


    @Test
    void createProductTest_ShouldReturnProductResponse(){
        CreateProductRequest request = new CreateProductRequest("Mock Product", "Good", BigDecimal.valueOf(756),
                4L, 5L, ProductStatus.ACTIVE, Set.of(2L));
        Tag t1 = new Tag("tech");
        Category c1 = new Category("Grocery");
        Manufacturer m1 = new Manufacturer("ubu", "35ngk53sn5e", StatusEnum.ACTIVE);

        Product mockProduct = new Product("Mock Product",BigDecimal.valueOf(756),"Good",
                c1, m1, ProductStatus.ACTIVE);
        ProductResponse response = new ProductResponse(2L, "Mock Product", "Good", BigDecimal.valueOf(756),
                c1.getName(), m1.getBrandName(), ProductStatus.ACTIVE, Set.of(t1.getTag()), null, OffsetDateTime.now());

        when(productMapper.toEntity(request)).thenReturn(mockProduct);
        when(manufacturerRepository.findById(any())).thenReturn(Optional.of(m1));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(c1));
        when(productMapper.toDto(mockProduct)).thenReturn(response);
        when(tagRepository.findById(any())).thenReturn(Optional.of(t1));
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        ProductResponse pr = productService.createProduct(request);

        assertThat(pr).isNotNull();
        assertThat(pr.name()).isEqualTo("Mock Product");
        assertThat(pr.manufacturerName()).isEqualTo("ubu");
        assertThat(pr.tagNames()).contains("tech");

        verify(productRepository).save(mockProduct);
        verify(productMapper).toDto(mockProduct);
        verify(productMapper).toEntity(request);
    }

    @Test
    void createProductTest_NoCategoryFound(){
        CreateProductRequest request = new CreateProductRequest("Mock", "Good", BigDecimal.valueOf(756),
                4L, 99L, ProductStatus.ACTIVE, Set.of(2L));

        Manufacturer m1 = new Manufacturer("ubu", "35ngk53sn5e", StatusEnum.ACTIVE);
        when(manufacturerRepository.findById(request.manufacturerId())).thenReturn(Optional.of(m1));
        when(categoryRepository.findById(request.categoryId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(CategoryNotFound.class)
                .hasMessageContaining("Category does not exist");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProductTest_NoManufacturerFound(){
        CreateProductRequest request = new CreateProductRequest("Mock", "Good", BigDecimal.valueOf(756),
                4L, 99L, ProductStatus.ACTIVE, Set.of(2L));

        when(manufacturerRepository.findById(request.manufacturerId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(ManufacturerNotFound.class)
                .hasMessageContaining("Manufacturer does not exist");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getProductsTest_WithoutCategoryOrTags(){
        Product p1 = new Product();
        p1.setName("Gaming Laptop");
        Product p2 = new Product();
        p2.setName("P2");

        ProductResponse response = new ProductResponse(
                100L,
                "Gaming Laptop",
                "High performance laptop",
                BigDecimal.valueOf(1500.50),
                "Electronics",
                "Asus",
                ProductStatus.ACTIVE,
                Set.of("tech", "gaming"),
                "http://example.com/image.png",
                OffsetDateTime.now()
        );

        List<ProductResponse> pr = List.of(response);
        List<Product> products = List.of(p1, p2);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(p1)).thenReturn(response);
        when(productMapper.toDto(p2)).thenReturn(null);

        List<ProductResponse> list = productService.getProducts(null, Set.of());

        verify(productRepository).findAll();
        verify(productRepository, never()).findByCategoryOrTags(anyLong(),any());
        verify(productMapper, times(2)).toDto(any(Product.class));
    }

    @Test
    void getProductsTest_WithCategoryOrTags(){
        Product p1 = new Product();
        p1.setName("Gaming Laptop");
        Product p2 = new Product();
        p2.setName("P2");

        ProductResponse response = new ProductResponse(
                100L,
                "Gaming Laptop",
                "High performance laptop",
                BigDecimal.valueOf(1500.50),
                "Electronics",
                "Asus",
                ProductStatus.ACTIVE,
                Set.of("tech", "gaming"),
                "http://example.com/image.png",
                OffsetDateTime.now()
        );

        List<ProductResponse> pr = List.of(response);
        List<Product> products = List.of(p1, p2);

        when(productRepository.findByCategoryOrTags(anyLong(), any())).thenReturn(products);
        when(productMapper.toDto(p1)).thenReturn(response);
        when(productMapper.toDto(p2)).thenReturn(null);

        List<ProductResponse> list = productService.getProducts(anyLong(), any());

        verify(productRepository, never()).findAll();
        verify(productRepository).findByCategoryOrTags(anyLong(),any());
        verify(productMapper, times(2)).toDto(any(Product.class));
    }

    @Test
    void getPendingApprovalsTest(){
        SellerProductBaseResponse response = new SellerProductBaseResponse(
                10L,
                "Mechanical Keyboard",
                55L,
                99L,
                "Tech Gadgets Hub",
                BigDecimal.valueOf(129.99),
                42,
                ProductStatus.PENDING,
                "https://example.com/keyboard.jpg"
        );
        Product p1 = new Product();
        p1.setName("Gaming Laptop");
        Users u1 = new Users();
        u1.setEmail("chaggu@gmail.com");
        Seller s1 = new Seller();
        s1.setUser(u1);
        List<SellerProduct> slr = List.of(new SellerProduct(p1, s1, BigDecimal.valueOf(75),12,ProductStatus.PENDING));
        List<SellerProductBaseResponse> response1 = List.of(response);

        when(sellerProductRepository.findAllPendingApprovals()).thenReturn(slr);
        when(sellerProductMapper.toBaseDto(any(SellerProduct.class))).thenReturn(response);

        List<SellerProductBaseResponse> result = productService.getPendingApprovals();

        verify(sellerProductRepository).findAllPendingApprovals();
        verify(sellerProductMapper).toBaseDto(any(SellerProduct.class));
    }

    @Test
    void approveListingTest_Accept(){
        Product p1 = new Product();
        p1.setName("Gaming Laptop");
        Users u1 = new Users();
        u1.setEmail("chaggu@gmail.com");
        Seller s1 = new Seller();
        s1.setUser(u1);
        SellerProduct slr = new SellerProduct(p1, s1, BigDecimal.valueOf(75),12,ProductStatus.PENDING);

        when(sellerProductRepository.findById(anyLong())).thenReturn(Optional.of(slr));

        String string = productService.approveListing(anyLong(), true);

        assertThat(string).isNotNull()
                        .isEqualTo("Application approved");
        assertThat(slr.getProductStatus()).isEqualTo(ProductStatus.ACTIVE);

        verify(sellerProductRepository).save(any(SellerProduct.class));
    }

    @Test
    void approveListingTest_Declined(){
        Product p1 = new Product();
        p1.setName("Gaming Laptop");
        Users u1 = new Users();
        u1.setEmail("chaggu@gmail.com");
        Seller s1 = new Seller();
        s1.setUser(u1);
        SellerProduct slr = new SellerProduct(p1, s1, BigDecimal.valueOf(75),12,ProductStatus.PENDING);

        when(sellerProductRepository.findById(anyLong())).thenReturn(Optional.of(slr));

        String string = productService.approveListing(anyLong(), false);

        assertThat(string).isNotNull()
                .isEqualTo("Application Rejected");
        assertThat(slr.getProductStatus()).isEqualTo(ProductStatus.REJECTED);
        verify(sellerProductRepository).save(any(SellerProduct.class));
    }

    @Test
    void approveListingTest_SellerProductNotFound(){
        when(sellerProductRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.approveListing(anyLong(), true))
                .isInstanceOf(SellerProductNotFound.class)
                .hasMessageContaining("Seller Product does not exist");

        verify(sellerProductRepository, never()).save(any(SellerProduct.class));
    }


    @Nested
    class SellerApplyToProductUsingSetup{
        Product p1;
        Users u1;
        SellerProduct sp1;
        Seller s1;

        @BeforeEach
        void setUp(){
            p1 = new Product();
            p1.setId(23L);
            p1.setName("Gaming Laptop");
            u1 = new Users();
            u1.setEmail("chaggu@gmail.com");
            u1.setId(4L);
            sp1 = new SellerProduct();
            sp1.setId(22L);
            s1 = new Seller();
            s1.setId(25L);
        }
        @Test
        void sellerApplyToProductTest_SellerProductPresent(){

            CreateSellerProductRequest request = new CreateSellerProductRequest(p1.getId(), BigDecimal.valueOf(79),12);

            when(sellerProductRepository.findByProductIdAndSellerId(anyLong(), anyLong())).thenReturn(Optional.of(sp1));
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(p1));
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u1));
            when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(s1));

            productService.sellerApplyToProduct(request, u1.getEmail());

            assertThat(sp1.getProductStatus()).isEqualTo(ProductStatus.PENDING);

            verify(sellerProductRepository).save(any(SellerProduct.class));
        }

        @Test
        void sellerApplyToProductTest_SellerProductNotPresent(){
            CreateSellerProductRequest request = new CreateSellerProductRequest(p1.getId(), BigDecimal.valueOf(79),12);

            when(sellerProductRepository.findByProductIdAndSellerId(anyLong(), anyLong())).thenReturn(Optional.empty());
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(p1));
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u1));
            when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(s1));

            when(sellerRepository.getReferenceById(s1.getId())).thenReturn(s1);

            productService.sellerApplyToProduct(request, u1.getEmail());

            // 4. Create the ArgumentCaptor "net"
            ArgumentCaptor<SellerProduct> captor = ArgumentCaptor.forClass(SellerProduct.class);

            // 5. Catch the new object when it gets passed to the save method
            verify(sellerProductRepository).save(captor.capture());

            // 6. Extract the newly created object
            SellerProduct savedListing = captor.getValue();

            assertThat(savedListing).isNotNull();
            assertThat(savedListing.getProduct()).isEqualTo(p1);
            assertThat(savedListing.getSeller()).isEqualTo(s1);
            assertThat(savedListing.getPrice()).isEqualTo(BigDecimal.valueOf(79));
            assertThat(savedListing.getQuantity()).isEqualTo(12);
            assertThat(savedListing.getProductStatus()).isEqualTo(ProductStatus.PENDING);
            verify(sellerProductRepository).save(any(SellerProduct.class));
        }

        @Test
        void sellerApplyToProduct_InsufficientStock(){

            CreateSellerProductRequest request = new CreateSellerProductRequest(p1.getId(), BigDecimal.valueOf(79),1200);

            when(sellerProductRepository.findByProductIdAndSellerId(anyLong(), anyLong())).thenReturn(Optional.of(sp1));
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(p1));
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u1));
            when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(s1));

            assertThatThrownBy(() -> productService.sellerApplyToProduct(request, u1.getEmail()))
                    .isInstanceOf(InsufficientStock.class)
                    .hasMessageContaining("Quantity should be less than max manufacturing limit");

            verify(sellerProductRepository, never()).save(any());
        }

        @Test
        void sellerApplyToProduct_UserDoesNotExist(){
            CreateSellerProductRequest request = new CreateSellerProductRequest(p1.getId(), BigDecimal.valueOf(79),1200);

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.sellerApplyToProduct(request, u1.getEmail()))
                    .isInstanceOf(UserDoesNotExist.class)
                    .hasMessageContaining("No such user exists");

            verify(sellerProductRepository, never()).save(any());
            verify(sellerRepository, never()).findById(anyLong());
        }

        @Test
        void sellerApplyToProduct_SellerNotFound(){
            CreateSellerProductRequest request = new CreateSellerProductRequest(p1.getId(), BigDecimal.valueOf(79),1200);

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u1));
            when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.sellerApplyToProduct(request, u1.getEmail()))
                    .isInstanceOf(SellerNotFound.class)
                    .hasMessageContaining("Seller profile not found for: " + u1.getEmail());

            verify(sellerProductRepository, never()).save(any());
            verify(sellerRepository).findById(anyLong());
            verify(sellerProductRepository, never()).findByProductIdAndSellerId(anyLong(), anyLong());
        }

        @Test
        void sellerApplyToProduct_ProductNotFound(){
            CreateSellerProductRequest request = new CreateSellerProductRequest(p1.getId(), BigDecimal.valueOf(79),12);

            when(sellerProductRepository.findByProductIdAndSellerId(anyLong(), anyLong())).thenReturn(Optional.of(sp1));
            when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u1));
            when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(s1));

            assertThatThrownBy(() -> productService.sellerApplyToProduct(request, u1.getEmail()))
                    .isInstanceOf(ProductNotFound.class)
                    .hasMessageContaining("Product not found");

            verify(sellerProductRepository, never()).save(any());
        }

    }

}
