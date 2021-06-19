package com.securevault.users.repositories;

import com.securevault.users.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryBasic  extends JpaRepository<Users, Integer> {

    Optional<Users> findUserByEmail(String email);

    Users findByUsername(String username);

    Optional<Users> findById(Integer userId);

//    @Query(value="select * FROM users", nativeQuery = true)
//    List<Users> test();
}