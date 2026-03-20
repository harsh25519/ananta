package dev.hkb.ananta.manufacturer;

import dev.hkb.ananta.manufacturer.dto.CreateManufacturerRequest;
import dev.hkb.ananta.manufacturer.dto.ManufacturerResponse;
import dev.hkb.ananta.product.ProductMapper;
import dev.hkb.ananta.product.ProductRepository;
import dev.hkb.ananta.product.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService{

    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ManufacturerMapper manufacturerMapper, ProductRepository productRepository, ProductMapper productMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerMapper = manufacturerMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    @Override
    public ManufacturerResponse addManufacturer(CreateManufacturerRequest cmr) {
        Manufacturer manufacturer = manufacturerMapper.toEntity(cmr);

        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    @Override
    public List<ManufacturerResponse> getAllManufacturers() {

        return manufacturerRepository.findAll()
                .stream()
                .map(manufacturerMapper::toDto)
                .toList();
    }

    @Override
    public List<ProductResponse> getProducts(String brandName) {
        Manufacturer manufacturer = manufacturerRepository.getManufacturerByBrandName(brandName)
                .orElseThrow(() -> new RuntimeException("Manufacturer Not Found"));

        return productRepository.findAllByManufacturerId(manufacturer.getId())
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteManufacturer(Long manufacturerId) {
        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found"));

        manufacturerRepository.delete(manufacturer);
    }

}
