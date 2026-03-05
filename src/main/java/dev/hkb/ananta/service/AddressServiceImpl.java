package dev.hkb.ananta.service;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.dao.AddressRepository;
import dev.hkb.ananta.dao.UserRepository;
import dev.hkb.ananta.dto.address.AddressResponse;
import dev.hkb.ananta.dto.address.CreateAddressRequest;
import dev.hkb.ananta.entity.Address;
import dev.hkb.ananta.entity.Users;
import dev.hkb.ananta.mapper.AddressMapper;
import dev.hkb.ananta.utils.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    private final AddressMapper addressMapper;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressMapper addressMapper, UserRepository userRepository, AddressRepository addressRepository) {
        this.addressMapper = addressMapper;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    @Override
    public AddressResponse addAddress(CreateAddressRequest car, UserPrincipal principal) {
        Address addr = addressMapper.toEntity(car);

        Users user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not authenticated."));

        if(user.getRole().equals(UserRoles.SELLER)){
            throw new RuntimeException("Seller is not authorized to add multiple addresses.");
        }

        addr.setUser(user);
        addressRepository.save(addr);

        return addressMapper.toDto(addr);
    }

    @Override
    public List<AddressResponse> getAddressList(UserPrincipal principal) {
        Users user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new AccessDeniedException("User not authenticated."));

        List<AddressResponse> addresses = addressRepository.findAllByUserId(user.getId())
                .stream()
                .map(addressMapper::toDto)
                .toList();

        return addresses;
    }

    @Transactional
    @Override
    public void deleteAddress(Long addrId, UserPrincipal principal) {

        Address addr = addressRepository.findByIdAndUserEmail(addrId, principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Address not found or unauthorized"));

        addressRepository.delete(addr);
    }
}
