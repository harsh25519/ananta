package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
