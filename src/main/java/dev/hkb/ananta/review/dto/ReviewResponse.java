package dev.hkb.ananta.review.dto;

public record ReviewResponse(Long id,
                             Long productId,
                             String productName,
                             Long customerId,
                             String customerName,
                             String comments,
                             int rating
) {
}
