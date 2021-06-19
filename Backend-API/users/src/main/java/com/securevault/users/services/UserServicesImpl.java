package com.securevault.users.services;

import com.securevault.users.exceptions.EtAuthException;
import com.securevault.users.model.Users;
import com.securevault.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServicesImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Override
    public Users validateUser(String email, String password) throws EtAuthException {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public Users registerUser(String first_name, String last_name, String email, String password) throws EtAuthException {
        String username = first_name+99;
        Integer userId = userRepository.create(first_name, last_name, username, email, password);

        return userRepository.findById(userId);
    }
}