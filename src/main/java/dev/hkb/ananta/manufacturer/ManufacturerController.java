package dev.hkb.ananta.manufacturer;

import dev.hkb.ananta.manufacturer.dto.CreateManufacturerRequest;
import dev.hkb.ananta.manufacturer.dto.ManufacturerResponse;
import dev.hkb.ananta.product.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    /// creation of manufacturer by admin
    @PostMapping
    public ResponseEntity<?> createManufacturer(@Valid @RequestBody CreateManufacturerRequest cmr){
        ManufacturerResponse mr = manufacturerService.addManufacturer(cmr);

        return new ResponseEntity<>(mr, HttpStatus.CREATED);
    }

    /// get manufacturer list by anyone
    @GetMapping
    public ResponseEntity<?> getManufacturerList(){
        List<ManufacturerResponse> list = manufacturerService.getAllManufacturers();
        return ResponseEntity.ok(list);
    }

    /// get list of products by a brand or manufacturer anyone can call
    @GetMapping("/{brandName}/products")
    public ResponseEntity<?> getProducts(@PathVariable String brandName){
        List<ProductResponse> products = manufacturerService.getProducts(brandName);
        return ResponseEntity.ok(products);
    }

    // delete a manufacturer
    @DeleteMapping("/{manufacturerId}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable Long manufacturerId){
        manufacturerService.deleteManufacturer(manufacturerId);

        return ResponseEntity.ok("Manufacturer Deleted");
    }


}
