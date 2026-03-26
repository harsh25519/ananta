package dev.hkb.ananta.review;

import dev.hkb.ananta.review.dto.CreateReviewRequest;
import dev.hkb.ananta.review.dto.ReviewResponse;
import dev.hkb.ananta.security.utils.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<?> giveReview(@PathVariable Long productId,
                                        @Valid @RequestBody CreateReviewRequest request,
                                        @AuthenticationPrincipal UserPrincipal principal){
        ReviewResponse reviewResponse = reviewService.writeReview(productId, request, principal.getUsername());
        return ResponseEntity.ok(reviewResponse);
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable Long productId){

        List<ReviewResponse> reviews = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getMyReviews(@AuthenticationPrincipal UserPrincipal principal){
        List<ReviewResponse> review = reviewService.getMyReviews(principal.getUsername());
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{productId}/reviews")
    public ResponseEntity<?> deleteReview(@PathVariable Long productId,
                                          @AuthenticationPrincipal UserPrincipal principal){
        reviewService.deleteReview(productId, principal.getUsername());
        return ResponseEntity.ok(Map.of("Message", "Review deleted"));
    }

}
