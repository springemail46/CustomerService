package com.spring.auth.repository;

import com.spring.auth.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Long> {
    UserData findByUsername(String username);
}
