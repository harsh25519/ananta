package dev.hkb.ananta.sellerProduct;

import dev.hkb.ananta.constants.ProductStatus;
import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.review.ReviewRepository;
import dev.hkb.ananta.seller.Seller;
import dev.hkb.ananta.seller.SellerRepository;
import dev.hkb.ananta.sellerProduct.dto.SellerProductBaseResponse;
import dev.hkb.ananta.sellerProduct.dto.SellerProductFullResponse;
import dev.hkb.ananta.sellerProduct.dto.UpdateSellerProductRequest;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class SellerProductServiceImpl implements SellerProductService{


    private final SellerProductRepository sellerProductRepository;
    private final SellerProductMapper sellerProductMapper;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public SellerProductServiceImpl(SellerProductRepository sellerProductRepository, SellerProductMapper sellerProductMapper, UserRepository userRepository, SellerRepository sellerRepository, ReviewRepository reviewRepository) {
        this.sellerProductRepository = sellerProductRepository;
        this.sellerProductMapper = sellerProductMapper;
        this.userRepository = userRepository;
        this.sellerRepository = sellerRepository;
        this.reviewRepository = reviewRepository;
    }

    // return complete list of products
    @Override
    public List<SellerProductBaseResponse> browseProducts(String productName, Long category, Set<Long> tags, String sortBy, boolean direction) {

        List<SellerProduct> products;

        Specification<SellerProduct> spec = Specification.where(SellerProductSpecs.hasStatus(ProductStatus.ACTIVE))
                .and(SellerProductSpecs.fetchProductAndSeller())
                .and(SellerProductSpecs.hasFuzzyName(productName))
                .and(SellerProductSpecs.hasCategory(category))
                .and(SellerProductSpecs.hasTags(tags));

        String sortField = (sortBy == null || sortBy.isBlank()) ? "price" : sortBy;
        Sort sort = Sort.by(direction ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);

        return sellerProductRepository.findAll(spec, sort)
                .stream()
                .map(sellerProductMapper::toBaseDto)
                .toList();
    }

    // return list of products by seller
    @Override
    public List<SellerProductBaseResponse> getProductList(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        Seller seller = sellerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User is not Seller"));

        Specification<SellerProduct> spec = Specification.where(SellerProductSpecs.hasSellerId(seller.getId()))
                .and(SellerProductSpecs.fetchProductAndSeller());

        return sellerProductRepository.findAll(spec)
                .stream()
                .map(sellerProductMapper::toBaseDto)
                .toList();
    }

    @Transactional
    @Override
    public SellerProductBaseResponse updateProduct(Long sellerProductId, UpdateSellerProductRequest request, String email) {

        SellerProduct product = sellerProductRepository.findById(sellerProductId)
                .orElseThrow(() -> new RuntimeException("Invalid SellerProduct id"));

        if(!product.getSeller().getUser().getEmail().equals(email)){
            throw new RuntimeException("The authenticated Seller is not authorized to do this.");
        }

        if(request.price() != null)product.setPrice(request.price());
        if(request.productStatus() != null){
            if (product.getProductStatus() == ProductStatus.REJECTED ||
                    product.getProductStatus() == ProductStatus.PENDING) {
                throw new RuntimeException("Cannot activate a product that is not approved.");
            }
            if(request.productStatus() == ProductStatus.OUT_OF_STOCK){
                product.setQuantity(0);
            }
            product.setProductStatus(request.productStatus());
        }

        sellerProductRepository.save(product);

        return sellerProductMapper.toBaseDto(product);
    }

    // return a single product
    @Override
    public SellerProductFullResponse showSellerProduct(Long sellerProductId, String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Specification<SellerProduct> spec = Specification.where(SellerProductSpecs.hasSellerProductId(sellerProductId))
                .and(SellerProductSpecs.fetchProductAndSeller());

        if(user.getRole().equals(UserRoles.CUSTOMER)){
            spec = spec.and(SellerProductSpecs.hasStatus(ProductStatus.ACTIVE));
        }

        // provide review and ratings
        Specification<SellerProduct> spec2 = Specification.where(SellerProductSpecs.hasSellerProductId(sellerProductId));

        Long productId = sellerProductRepository.findOne(spec2)
                .orElseThrow(() -> new RuntimeException("Product does not exist"))
                .getProduct()
                .getId();

        Double avgRating = reviewRepository.findAverageRatingByProductId(productId);
        Long totalRatings = reviewRepository.countReviewByProductId(productId);

        return sellerProductRepository.findOne(spec)
                .map(x -> sellerProductMapper.toFullDto(x, avgRating, totalRatings))
                .orElseThrow(() -> new RuntimeException("Product not found or currently unavailable"));

    }

//    @Transactional
//    @Override
//    public void deleteProduct(Long sellerProductId, String email) {
//
//        SellerProduct sellerProduct = sellerProductRepository.findById(sellerProductId)
//                .orElseThrow(() -> new RuntimeException("Seller product does not exist"));
//
//        if(!sellerProduct.getSeller().getUser().getEmail().equals(email)){
//            throw new RuntimeException("User is not authorized");
//        }
//
//        sellerProductRepository.delete(sellerProduct);
//    }

}
