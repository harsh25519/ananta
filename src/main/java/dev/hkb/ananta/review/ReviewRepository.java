package dev.hkb.ananta.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId")Long productId);

    Long countReviewByProductId(Long productId);

    List<Review> findAllByProductId(Long productId);

    List<Review> findAllByUserId(Long userId);

    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);
}
