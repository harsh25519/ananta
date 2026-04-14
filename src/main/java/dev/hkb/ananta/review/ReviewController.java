package dev.hkb.ananta.review;

import dev.hkb.ananta.review.dto.CreateReviewRequest;
import dev.hkb.ananta.review.dto.ReviewResponse;
import dev.hkb.ananta.security.utils.UserPrincipal;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /// Create Review for a product by CUSTOMER ONLY
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<?> giveReview(@PathVariable Long productId,
                                        @Valid @RequestBody CreateReviewRequest request,
                                        @AuthenticationPrincipal UserPrincipal principal){
        ReviewResponse reviewResponse = reviewService.writeReview(productId, request, principal.getUsername());
        return ResponseEntity.ok(reviewResponse);
    }

    /// Get list of reviews for a particular product can be accessed by anyone
    @GetMapping("/{productId}/reviews")
    @SecurityRequirements()
    public ResponseEntity<?> getReviews(@PathVariable Long productId){

        List<ReviewResponse> reviews = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(reviews);
    }

    /// Get a list of all reviews done by me
    @GetMapping("/reviews")
    public ResponseEntity<?> getMyReviews(@AuthenticationPrincipal UserPrincipal principal){
        List<ReviewResponse> review = reviewService.getMyReviews(principal.getUsername());
        return ResponseEntity.ok(review);
    }

    /// Customer can delete his or her review
    @DeleteMapping("/{productId}/reviews")
    public ResponseEntity<?> deleteReview(@PathVariable Long productId,
                                          @AuthenticationPrincipal UserPrincipal principal){
        reviewService.deleteReview(productId, principal.getUsername());
        return ResponseEntity.ok(Map.of("Message", "Review deleted"));
    }

}
