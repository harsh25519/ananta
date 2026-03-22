package dev.hkb.ananta.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
