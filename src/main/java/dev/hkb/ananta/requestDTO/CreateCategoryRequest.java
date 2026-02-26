package dev.hkb.ananta.requestDTO;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @NotBlank String name
) {
}
