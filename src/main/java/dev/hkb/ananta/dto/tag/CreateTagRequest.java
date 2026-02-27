package dev.hkb.ananta.dto.tag;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(
        @NotBlank String tag
) {
}
