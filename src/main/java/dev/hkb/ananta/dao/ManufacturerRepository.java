package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    
}
