package com.securevault.users.repositories;

import com.securevault.users.exceptions.EtAuthException;
import com.securevault.users.model.Users;

public interface UserRepository {

    Integer create(String first_name, String last_name, String username, String email, String password) throws EtAuthException;

    Users findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    Users findById(Integer userId);
}
