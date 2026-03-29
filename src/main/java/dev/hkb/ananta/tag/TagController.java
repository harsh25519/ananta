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

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /// Admin or Seller will create tags for the products
    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody CreateTagRequest ctr){
        TagResponse tag = tagService.addTag(ctr);
        return ResponseEntity.ok(tag);
    }

    /// Any user can get all tags
    @GetMapping
    public ResponseEntity<?> getAllTags(){
        List<TagResponse> tags = tagService.getTags();
        return ResponseEntity.ok(tags);
    }

    ///  Delete tags and remove from products (ADMIN ONLY)
    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId){
        tagService.deleteTag(tagId);
        return ResponseEntity.ok("Tag deleted");
    }
}
