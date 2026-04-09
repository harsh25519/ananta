package dev.hkb.ananta.seller;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.seller.dto.CreateSellerRequest;
import dev.hkb.ananta.seller.dto.SellerResponse;
import dev.hkb.ananta.user.UserMapper;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerServiceImpl implements SellerService{

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final UserMapper userMapper;

    public SellerServiceImpl(UserRepository userRepository, SellerRepository sellerRepository, SellerMapper sellerMapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public SellerResponse applyForSeller(CreateSellerRequest sellerDto, String email) {
        Users ur = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFound("Authenticated user does not exists in database"));
                .orElseThrow(() -> new RuntimeException("Authenticated user does not exists in database"));

        if(sellerRepository.existsByUser(ur) || ur.getRole().equals(UserRoles.SELLER)){
//            throw new SellerAlreadyExistsException("Seller already exists with these user credentials.");
            throw new RuntimeException("Seller already exists with these user credentials.");
        }

        Seller seller = sellerMapper.toEntity(sellerDto);
        seller.setUser(ur);
        Seller s = sellerRepository.save(seller);
        ur.setRole(UserRoles.SELLER);
        SellerResponse response = sellerMapper.toDto(seller);
        return response;
    }

    @Override
    public SellerResponse findByEmail(String username) {
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User does not exits."));

        Seller seller = sellerRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Authenticated user is not seller"));

        return sellerMapper.toDto(seller);
    }
}
