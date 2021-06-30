package com.securevault.users.services;

import com.securevault.users.exceptions.EtAuthException;
import com.securevault.users.model.Users;

public interface UserService {

    Users validateUser(String email, String password) throws EtAuthException;

    Users registerUser(String firstName, String lastName,  String email, String password) throws EtAuthException;

}
