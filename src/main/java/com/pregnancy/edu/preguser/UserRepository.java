package com.pregnancy.edu.preguser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByEmail(String email);

    Optional<MyUser> findByUsername(String username);
}
