package dev.hkb.ananta.order;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("Select distinct o from Orders o join fetch o.orderItemList  where o.user.id = :userId")
    List<Orders> findAllByUser_Id(@Param("userId")Long userId);

}
