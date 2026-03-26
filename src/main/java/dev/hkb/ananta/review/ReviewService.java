package dev.hkb.ananta.review;

import dev.hkb.ananta.review.dto.CreateReviewRequest;
import dev.hkb.ananta.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse writeReview(Long productId, CreateReviewRequest request, String email);

    List<ReviewResponse> getProductReviews(Long productId);

    List<ReviewResponse> getMyReviews(String email);

    void deleteReview(Long productId, String email);

}
