package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT DISTINCT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email")String email);
}
