package dev.hkb.ananta.product;

import dev.hkb.ananta.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByManufacturerId(Long id);

    List<Product> findAllByCategoryId(Long categoryId);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN p.tagSet t " +
            "WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "OR (t.id IN :tagIds)")
    List<Product> findByCategoryOrTags(@Param("categoryId") Long categoryId,
                                       @Param("tagIds") Set<Long> tagIds);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.category = :newCat WHERE p.category.id = :oldCatId")
    void updateCategoryForProducts(@Param("oldCatId")Long categoryId, @Param("newCat")Category generalCategory);

    List<Product> findAllByTagSet_Id(Long tagSetId);
}
