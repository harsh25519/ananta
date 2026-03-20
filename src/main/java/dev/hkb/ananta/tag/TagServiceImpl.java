package dev.hkb.ananta.tag;


import dev.hkb.ananta.product.ProductMapper;
import dev.hkb.ananta.product.ProductRepository;
import dev.hkb.ananta.tag.dto.CreateTagRequest;
import dev.hkb.ananta.tag.dto.TagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    private TagRepository tagRepository;
    private TagMapper tagMapper;
    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper,
                          ProductRepository productRepository, ProductMapper productMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
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
                .orElseThrow(() -> new RuntimeException("Tag does not exist"));
        tagRepository.delete(tag);
    }
}
