package dev.hkb.ananta.tag;

import dev.hkb.ananta.tag.dto.CreateTagRequest;
import dev.hkb.ananta.tag.dto.TagResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag toEntity(CreateTagRequest ctr);

    TagResponse toDto(Tag tag);
}
