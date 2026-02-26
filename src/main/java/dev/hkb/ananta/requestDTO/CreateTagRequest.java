package dev.hkb.ananta.requestDTO;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(
        @NotBlank String tag
) {
}
