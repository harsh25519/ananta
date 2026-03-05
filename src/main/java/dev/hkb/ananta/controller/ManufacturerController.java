package dev.hkb.ananta.controller;

import dev.hkb.ananta.dto.manufacturer.CreateManufacturerRequest;
import dev.hkb.ananta.dto.manufacturer.ManufacturerResponse;
import dev.hkb.ananta.service.ManufacturerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manufacturers")
@PreAuthorize("hasRole('ADMIN')")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping
    public ResponseEntity<?> createManufacturer(@Valid @RequestBody CreateManufacturerRequest cmr){
        ManufacturerResponse mr = manufacturerService.addManufacturer(cmr);

        return new ResponseEntity<>(mr, HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<?> getManufacturerList(){
//
//    }

}
