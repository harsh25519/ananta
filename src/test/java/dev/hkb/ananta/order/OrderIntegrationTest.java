package dev.hkb.ananta.order;


import dev.hkb.ananta.address.Address;
import dev.hkb.ananta.address.AddressRepository;
import dev.hkb.ananta.cart.Cart;
import dev.hkb.ananta.cart.CartItem;
import dev.hkb.ananta.cart.CartRepository;
import dev.hkb.ananta.category.Category;
import dev.hkb.ananta.category.CategoryRepository;
import dev.hkb.ananta.constants.*;
import dev.hkb.ananta.manufacturer.Manufacturer;
import dev.hkb.ananta.manufacturer.ManufacturerRepository;
import dev.hkb.ananta.order.dto.CreateOrderRequest;
import dev.hkb.ananta.product.Product;
import dev.hkb.ananta.product.ProductRepository;
import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.seller.Seller;
import dev.hkb.ananta.sellerProduct.SellerProduct;
import dev.hkb.ananta.sellerProduct.SellerProductRepository;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerProductRepository sellerProductRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void checkoutFlow_ShouldCreateOrderFromCart() throws Exception {

        Users user = new Users("Order", "Tester", "order@test.com", "pass",
                "9451268123", UserRoles.CUSTOMER);
        user = userRepository.saveAndFlush(user);
        UserPrincipal principal = new UserPrincipal(user);

        Seller seller = new Seller(user, "abc","345tfgder4es","xyzgtf","ertyu",
                StateEnum.HP,"751235", CountryEnum.IN);

        Address addr = new Address(user,"9481513568","123 Street", "City",
                StateEnum.HP, "177001", CountryEnum.IN);
        addr = addressRepository.saveAndFlush(addr);

        Category c1 = categoryRepository.saveAndFlush(new Category("Tech"));

        Manufacturer m1 = manufacturerRepository.saveAndFlush(new Manufacturer("Intel", "LIC-123", StatusEnum.ACTIVE));

        Product p1 = new Product("Core i9", BigDecimal.valueOf(50000), "Fast CPU", c1, m1, ProductStatus.ACTIVE);
        p1 = productRepository.saveAndFlush(p1);

        SellerProduct sp = new SellerProduct();
        sp.setProduct(p1);
        sp.setSeller(seller);
        sp.setPrice(BigDecimal.valueOf(48000));
        sp.setQuantity(100);
        sp.setProductStatus(ProductStatus.ACTIVE);

        sellerProductRepository.saveAndFlush(sp);

        Cart cart = new Cart();
        cart.setUser(user);
        CartItem item = new CartItem(cart, sp, 2, sp.getPrice());
        cart.setCartItems(List.of(item));
        cartRepository.saveAndFlush(cart);

        CreateOrderRequest request = new CreateOrderRequest(addr.getId(), addr.getId());
        String jsonBody = jsonMapper.writeValueAsString(request);

        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .with(user(principal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                .andExpect(jsonPath("$.orderItemList.length()").value(1))
                .andExpect(jsonPath("$.totalPrice").value(96000));

        List<Orders> savedOrders = orderRepository.findAllByUser_Id(user.getId());
        assertThat(savedOrders).hasSize(1);
    }
}
