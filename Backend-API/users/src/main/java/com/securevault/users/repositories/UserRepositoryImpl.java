package com.securevault.users.repositories;

import com.securevault.users.exceptions.EtAuthException;
import com.securevault.users.model.Users;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryBasic userRepositoryBasic;

    public UserRepositoryImpl(UserRepositoryBasic userRepositoryBasic) {
        this.userRepositoryBasic = userRepositoryBasic;
    }


    @Override
    public Integer create(String first_name, String last_name, String username, String email, String password) throws EtAuthException {
        try{
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
            Users insertData = userRepositoryBasic.save(new Users(first_name, last_name, username, email, hashedPassword, true));
            return insertData.getId();
        }catch (Exception e){
            if(e.getClass().getName().toString() == "javax.validation.ConstraintViolationException"){
                throw new EtAuthException(handleConstraintViolation((ConstraintViolationException) e).toString());
            }else{
                throw new EtAuthException(e.getMessage());
            }
        }
    }

    public Integer checkEmailID(String email){
        Users user = userRepositoryBasic.findUserByEmail(email).orElse(null);
        if(user == null){
            return 0;
        }

        return user.getId();
    }


    @ExceptionHandler({ ConstraintViolationException.class })
    public List<String> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<String>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        return errors;
    }

    @Override
    public Users findByEmailAndPassword(String email, String password) throws EtAuthException {

        try {
            Users user = userRepositoryBasic.findUserByEmail(email).orElse(null);
            if(BCrypt.checkpw(password, user.getPassword())){
                return user;
            }else{
                throw new EtAuthException("Invalid email/password");
            }
        }catch (EmptyResultDataAccessException e) {
            throw new EtAuthException("Invalid email/password");
        }

    }

    @Override
    public Integer getCountByEmail(String email) {
        return null;
    }

    @Override
    public Users findById(Integer userId) {
        Users newUser = userRepositoryBasic.findById(userId).orElse(null);

        return newUser;
    }
}
