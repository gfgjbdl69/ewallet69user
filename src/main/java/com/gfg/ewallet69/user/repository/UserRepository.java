package com.gfg.ewallet69.user.repository;

import com.gfg.ewallet69.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByName(String name);
}
