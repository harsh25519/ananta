package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
