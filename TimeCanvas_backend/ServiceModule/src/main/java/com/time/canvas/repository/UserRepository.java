package com.time.canvas.repository;

import com.time.canvas.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,  Long> {
    Optional<User> findByUserAccount(String userAccount);

}