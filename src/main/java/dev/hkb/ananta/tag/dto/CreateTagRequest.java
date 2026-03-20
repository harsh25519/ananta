package dev.hkb.ananta.tag.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(
        @NotBlank String tag
) {
}
