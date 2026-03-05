package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUserId(Long id);

    Optional<Address> findByIdAndUserEmail(Long addrId, String username);
}
