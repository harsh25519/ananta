package dev.hkb.ananta.manufacturer;

import dev.hkb.ananta.manufacturer.dto.CreateManufacturerRequest;
import dev.hkb.ananta.manufacturer.dto.ManufacturerResponse;
import dev.hkb.ananta.product.dto.ProductResponse;

import java.util.List;

public interface ManufacturerService {

    ManufacturerResponse addManufacturer(CreateManufacturerRequest cmr);

    List<ManufacturerResponse> getAllManufacturers();

    List<ProductResponse> getProducts(String brandName);

    void deleteManufacturer(Long manufacturerId);
}
