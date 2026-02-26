package dev.hkb.ananta.responseDTO;

public record ReviewResponse(Long id,
                             Long productId,
                             String productName,
                             Long customerId,
                             String customerName,
                             String comments,
                             int rating
) {
}
