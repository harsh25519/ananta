package dev.hkb.ananta.sellerProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SellerProductRepository extends JpaRepository<SellerProduct, Long>,
                                                    JpaSpecificationExecutor<SellerProduct> {

    Optional<SellerProduct> findByProductIdAndSellerId(Long productId, Long sellerId);

    @Query("SELECT DISTINCT sp FROM SellerProduct sp " +
            "JOIN FETCH sp.product p " +
            "JOIN FETCH sp.seller as s " +
            "JOIN FETCH s.user as u " +
            "WHERE sp.productStatus = 'PENDING'"
    )
    List<SellerProduct> findAllPendingApprovals();

}
