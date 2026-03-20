package dev.hkb.ananta.tag;

import dev.hkb.ananta.tag.dto.CreateTagRequest;
import dev.hkb.ananta.tag.dto.TagResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody CreateTagRequest ctr){
        TagResponse tag = tagService.addTag(ctr);
        return ResponseEntity.ok(tag);
    }

    @GetMapping
    public ResponseEntity<?> getAllTags(){
        List<TagResponse> tags = tagService.getTags();
        return ResponseEntity.ok(tags);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId){
        tagService.deleteTag(tagId);
        return ResponseEntity.ok("Tag deleted");
    }
}
