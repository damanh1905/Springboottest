package com.tutorial.apitest.repositories;

import com.tutorial.apitest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByEmail(String userEmail);
}
