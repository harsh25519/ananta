package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    Optional<Manufacturer> getManufacturerByBrandName(String brandName);
}
