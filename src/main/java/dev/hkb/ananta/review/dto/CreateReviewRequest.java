package dev.hkb.ananta.review.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record CreateReviewRequest(
                                  @Size(max = 500)
                                  String comments,

                                  @NotNull
                                  @Range(min = 1, max = 5, message = "Enter a valid rating")
                                  Integer rating
) {
}
