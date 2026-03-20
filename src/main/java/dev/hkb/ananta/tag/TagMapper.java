package dev.hkb.ananta.mapper;

import dev.hkb.ananta.dto.tag.CreateTagRequest;
import dev.hkb.ananta.dto.tag.TagResponse;
import dev.hkb.ananta.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag toEntity(CreateTagRequest ctr);

    TagResponse toDto(Tag tag);
}
