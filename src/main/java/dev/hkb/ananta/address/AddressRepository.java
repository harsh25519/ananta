package dev.hkb.ananta.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUserId(Long id);

    Optional<Address> findByIdAndUserEmail(Long addrId, String username);

    @Modifying
    @Query("DELETE FROM Address a where a.user.id = :user_id")
    void deleteByUser_Id(@Param("user_id")Long userId);

}
