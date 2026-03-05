package dev.hkb.ananta.service;

import dev.hkb.ananta.dao.ManufacturerRepository;
import dev.hkb.ananta.dto.manufacturer.CreateManufacturerRequest;
import dev.hkb.ananta.dto.manufacturer.ManufacturerResponse;
import dev.hkb.ananta.entity.Manufacturer;
import dev.hkb.ananta.mapper.ManufacturerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManufacturerServiceImpl implements ManufacturerService{

    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ManufacturerMapper manufacturerMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerMapper = manufacturerMapper;
    }

    @Transactional
    @Override
    public ManufacturerResponse addManufacturer(CreateManufacturerRequest cmr) {
        Manufacturer manufacturer = manufacturerMapper.toEntity(cmr);

        manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }
}
