package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
