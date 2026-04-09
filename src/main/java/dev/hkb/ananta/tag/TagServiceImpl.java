package dev.hkb.ananta.tag;


import dev.hkb.ananta.exceptionHandler.TagNotFound;
import dev.hkb.ananta.product.Product;
import dev.hkb.ananta.product.ProductRepository;
import dev.hkb.ananta.tag.dto.CreateTagRequest;
import dev.hkb.ananta.tag.dto.TagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final ProductRepository productRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper, ProductRepository productRepository) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public TagResponse addTag(CreateTagRequest ctr) {
        Tag tag = tagMapper.toEntity(ctr);
        tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public List<TagResponse> getTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFound("Tag not found"));

        List<Product> products = productRepository.findAllByTagSet_Id(tagId);

        for(Product p : products){
            p.getTagSet().remove(tag);
        }
        productRepository.saveAll(products);

        tagRepository.delete(tag);
    }
}
