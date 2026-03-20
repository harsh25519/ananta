package dev.hkb.ananta.security.utils;

import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepo;

    public UserDetailsServiceImpl(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrincipal(user);
    }
}
