package dev.hkb.ananta.manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    Optional<Manufacturer> getManufacturerByBrandName(String brandName);
}
