package dev.hkb.ananta.review;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.product.Product;
import dev.hkb.ananta.product.ProductRepository;
import dev.hkb.ananta.review.dto.CreateReviewRequest;
import dev.hkb.ananta.review.dto.ReviewResponse;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{


    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(UserRepository userRepository, ReviewMapper reviewMapper, ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    @Override
    public ReviewResponse writeReview(Long productId, CreateReviewRequest request, String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        if(!user.getRole().equals(UserRoles.CUSTOMER)){
            throw new RuntimeException("User is not authorized to write review.");
        }

        Review review = reviewMapper.toEntity(request);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("No product found."));

        review.setUser(user);
        review.setProduct(product);

        reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    @Override
    public List<ReviewResponse> getProductReviews(Long productId) {

        return reviewRepository.findAllByProductId(productId)
                .stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    public List<ReviewResponse> getMyReviews(String email) {
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not allowed"))
                .getId();

        return reviewRepository.findAllByUserId(userId)
                .stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteReview(Long productId, String email) {
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not allowed"))
                .getId();

        Review review = reviewRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewRepository.delete(review);
    }


}
