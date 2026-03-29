package dev.hkb.ananta.address;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.address.dto.AddressResponse;
import dev.hkb.ananta.address.dto.CreateAddressRequest;
import dev.hkb.ananta.user.Users;
import dev.hkb.ananta.security.utils.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

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

        if(user.getRole().equals(UserRoles.SELLER)){
            throw new RuntimeException("Seller is not authorized to access multiple addresses.");
        }

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
