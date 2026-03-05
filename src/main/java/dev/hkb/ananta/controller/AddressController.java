package dev.hkb.ananta.controller;

import dev.hkb.ananta.dto.address.AddressResponse;
import dev.hkb.ananta.dto.address.CreateAddressRequest;
import dev.hkb.ananta.service.AddressService;
import dev.hkb.ananta.utils.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {


    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<?> getAddressList(@AuthenticationPrincipal UserPrincipal principal){
        List<AddressResponse> addresses = addressService.getAddressList(principal);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<?> addAddress(@Valid @RequestBody CreateAddressRequest car,
                                        @AuthenticationPrincipal UserPrincipal principal){
        AddressResponse addr = addressService.addAddress(car, principal);
        return new ResponseEntity<>(addr, HttpStatus.CREATED);
    }

    @DeleteMapping("/{addrId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addrId,
                                           @AuthenticationPrincipal UserPrincipal principal){
        addressService.deleteAddress(addrId, principal);
        return ResponseEntity.ok("Address Deleted");
    }

}
