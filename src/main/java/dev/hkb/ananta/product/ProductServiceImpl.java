package dev.hkb.ananta.product;

import dev.hkb.ananta.category.Category;
import dev.hkb.ananta.category.CategoryRepository;
import dev.hkb.ananta.constants.ProductStatus;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService{

    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final TagRepository tagRepository;
    private final SellerProductRepository sellerProductRepository;
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final SellerProductMapper sellerProductMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
                              ManufacturerRepository manufacturerRepository, CategoryRepository categoryRepository,
                              TagRepository tagRepository, SellerProductRepository sellerProductRepository,
                              SellerRepository sellerRepository, UserRepository userRepository,
                              SellerProductMapper sellerProductMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.sellerProductRepository = sellerProductRepository;
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
        this.sellerProductMapper = sellerProductMapper;
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

    @Override
    public List<ProductResponse> getProducts(Long category, Set<Long> tag) {

        List<Product> product;

        // to find by filter or category and tags to apply for sell
        if(category == null && (tag == null || tag.isEmpty())){
            product = productRepository.findAll();
        }else{
            product = productRepository.findByCategoryOrTags(category, tag);
        }

        return product.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void sellerApplyToProduct(CreateSellerProductRequest request, String sellerMail) {
        Users user = userRepository.findByEmail(sellerMail)
                .orElseThrow(()->new RuntimeException("No such user exists"));

        Seller seller = sellerRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Seller profile not found for: " + sellerMail));

        Optional<SellerProduct> existing = sellerProductRepository.findByProductIdAndSellerId(request.productId(), seller.getId());

        SellerProduct listing;
        Product masterProduct = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (existing.isPresent()) {
            listing = existing.get();

            int qty = listing.getQuantity() + request.quantity();
            if(qty > masterProduct.getMaxManufacturingLimit()){
                throw new RuntimeException("Quantity should be less than max manufacturing limit");
            }

            listing.setQuantity(qty);
            listing.setProductStatus(ProductStatus.PENDING);
        }else{
            listing = new SellerProduct();
            listing.setProduct(masterProduct);
            listing.setSeller(sellerRepository.getReferenceById(seller.getId()));
            listing.setQuantity(request.quantity());
            listing.setProductStatus(ProductStatus.PENDING);
        }

        listing.setPrice(request.price());
        sellerProductRepository.save(listing);
    }

    @Override
    public List<SellerProductBaseResponse> getPendingApprovals() {
        return sellerProductRepository.findAllPendingApprovals()
                .stream()
                .map(sellerProductMapper::toBaseDto)
                .toList();
    }

    @Transactional
    @Override
    public String approveListing(Long sellerProductId, boolean approved) {
        SellerProduct sellerProduct = sellerProductRepository.findById(sellerProductId)
                .orElseThrow(() -> new RuntimeException("Seller Product does not exist"));

        if(approved){
            sellerProduct.setProductStatus(ProductStatus.ACTIVE);
        }else{
            sellerProduct.setProductStatus(ProductStatus.REJECTED);
        }

        sellerProductRepository.save(sellerProduct);
        if(approved){
            return "Application approved";
        }else{
            return "Application Rejected";
        }
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
