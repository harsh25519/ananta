package dev.hkb.ananta.address;

import dev.hkb.ananta.address.dto.AddressResponse;
import dev.hkb.ananta.address.dto.CreateAddressRequest;
import dev.hkb.ananta.security.utils.UserPrincipal;
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

    /// It returns a list of address assigned to the logged in User
    @GetMapping
    public ResponseEntity<?> getAddressList(@AuthenticationPrincipal UserPrincipal principal){
        List<AddressResponse> addresses = addressService.getAddressList(principal);
        return ResponseEntity.ok(addresses);
    }

    /// Only Customer can add Address for themselves
    @PostMapping
    public ResponseEntity<?> addAddress(@Valid @RequestBody CreateAddressRequest car,
                                        @AuthenticationPrincipal UserPrincipal principal){
        AddressResponse addr = addressService.addAddress(car, principal);
        return new ResponseEntity<>(addr, HttpStatus.CREATED);
    }

    /// Customer can delete address which is not required
    /// Might create an orphan child
//    @DeleteMapping("/{addrId}")
//    public ResponseEntity<?> deleteAddress(@PathVariable Long addrId,
//                                           @AuthenticationPrincipal UserPrincipal principal){
//        addressService.deleteAddress(addrId, principal);
//        return ResponseEntity.ok("Address Deleted");
//    }

}
