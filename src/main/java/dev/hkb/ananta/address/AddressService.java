package dev.hkb.ananta.address;

import dev.hkb.ananta.address.dto.AddressResponse;
import dev.hkb.ananta.address.dto.CreateAddressRequest;
import dev.hkb.ananta.security.utils.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressResponse addAddress(@Valid CreateAddressRequest car, UserPrincipal principal);

    List<AddressResponse> getAddressList(UserPrincipal principal);

    void deleteAddress(Long addrId, UserPrincipal principal);
}
