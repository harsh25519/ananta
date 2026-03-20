package dev.hkb.ananta.tag;

import dev.hkb.ananta.tag.dto.CreateTagRequest;
import dev.hkb.ananta.tag.dto.TagResponse;

import java.util.List;

public interface TagService {

    TagResponse addTag(CreateTagRequest ctr);

    List<TagResponse> getTags();

    void deleteTag(Long tagId);

}
