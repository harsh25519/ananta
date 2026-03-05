package dev.hkb.ananta.service;

import dev.hkb.ananta.dto.address.AddressResponse;
import dev.hkb.ananta.dto.address.CreateAddressRequest;
import dev.hkb.ananta.utils.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressResponse addAddress(@Valid CreateAddressRequest car, UserPrincipal principal);

    List<AddressResponse> getAddressList(UserPrincipal principal);

    void deleteAddress(Long addrId, UserPrincipal principal);
}
