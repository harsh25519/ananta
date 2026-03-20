package dev.hkb.ananta.service;

import dev.hkb.ananta.dto.manufacturer.CreateManufacturerRequest;
import dev.hkb.ananta.dto.manufacturer.ManufacturerResponse;
import dev.hkb.ananta.product.dto.ProductResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ManufacturerService {
    ManufacturerResponse addManufacturer(@Valid CreateManufacturerRequest cmr);

    List<ManufacturerResponse> getAllManufacturers();

    List<ProductResponse> getProducts(String brandName);

    void deleteManufacturer(Long manufacturerId);
}
