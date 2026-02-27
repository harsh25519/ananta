package dev.hkb.ananta.dto.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record CreateReviewRequest(@NotNull
                                  @Positive
                                  Long productId,

                                  @NotNull
                                  @Positive
                                  Long customerId,

                                  @Size(max = 500) String comments,

                                  @NotNull
                                  @Range(min = 1, max = 5, message = "Enter a valid rating")
                                  Integer ratings
) {
}
