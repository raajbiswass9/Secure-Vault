package com.securevault.users.services;

import com.securevault.users.exceptions.EtAuthException;
import com.securevault.users.model.Users;
import com.securevault.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;
import java.util.regex.Pattern;

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

        //Check if payload parameters are correct and not empty
        if(first_name == null || last_name == null || email == null || password == null){
            throw new EtAuthException("Unable to register. Invalid parameters passed");
        }else if(first_name == "" && last_name  == "" && email  == "" && password == "" ){
            throw new EtAuthException("Unable to register. Invalid email ID provided");
        }

        //Check if firstname and lastname are valid
        if((Pattern.matches("[a-zA-Z ]*",first_name) == false) || (Pattern.matches("[a-zA-Z ]*",last_name) == false)){
            throw new EtAuthException("Unable to register. Firstname and Lastname can only contain letters");
        }else if(first_name.length() < 2 || last_name.length() < 2){
            throw new EtAuthException("Unable to register. Firstname and Lastname should be atleast 2 letters");
        }

        //Check if email is valid
        if(Pattern.matches("^(.+)@(.+)$",email) == false){
            throw new EtAuthException("Unable to register. Invalid email ID provided");
        }

        //Capitalize firstname and lastname
        first_name = capitalize(first_name);
        last_name = capitalize(last_name);

        //Generate username
        String username = generateUsername(first_name);

        //Check if email ID already exists
        if(userRepository.checkEmailID(email) > 0){
            throw new EtAuthException("Unable to register. Email ID already exits");
        }

        //Create user and return user ID
        Integer userId = userRepository.create(first_name, last_name, username, email, password);

        return userRepository.findById(userId);
    }


    public String generateUsername(String firstname){
        firstname = firstname.toLowerCase().trim();
        Random rand = new Random();
        int random = rand.nextInt(999 + 1 - 100) + 100;
        char ch1 = firstname.charAt(0);
        char ch2 = firstname.charAt(1);
        return ch1+""+ch2+""+random;
    }

    public static String capitalize(String source) {
        String new_source = source.toLowerCase();
        String[] splited = new_source.split(" ");
        String name = "";

        for(String str : splited){
            name = name + str.substring(0, 1).toUpperCase() + str.substring(1)+" ";
        }
        return name;
    }

}