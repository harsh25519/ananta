package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
