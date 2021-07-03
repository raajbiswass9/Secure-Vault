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

    /**
     * Login user
     * @param email
     * @param password
     * @return User object
     * @throws EtAuthException
     */
    @Override
    public Users validateUser(String email, String password) throws EtAuthException {
        //Check if payload parameters are correct and not empty
         verifyEamilPassPayload(email, password);

        //Check if email ID  is in correct format
        verifyEmailFormat(email);

        return userRepository.findByEmailAndPassword(email, password);
    }

    /**
     * Register new user
     * @param first_name
     * @param last_name
     * @param email
     * @param password
     * @return User object
     * @throws EtAuthException
     */
    @Override
    public Users registerUser(String first_name, String last_name, String email, String password) throws EtAuthException {

        //Check if payload parameters are correct and not empty
        verifyNamePayload(first_name, last_name);
        verifyEamilPassPayload(email, password);

        //Check if firstname and lastname is in correct format
        verifyNameFormat(first_name, last_name);
        //Check if email ID  is in correct format
        verifyEmailFormat(email);

        //Capitalize firstname and lastname
        first_name = capitalize(first_name);
        last_name = capitalize(last_name);

        //Generate username
        String username = generateUsername(first_name);

        //Check if email ID already exists
        if(userRepository.getCountByEmail(email) > 0){
            throw new EtAuthException("Unable to register. Email ID already exits");
        }

        //Create user and return user ID
        Integer userId = userRepository.create(first_name, last_name, username, email, password);

        return userRepository.findById(userId);
    }

    /**
     * Verify Payload Parameters (First name and Last name)
     * @param first_name
     * @param last_name
     */
    public void verifyNamePayload(String first_name, String last_name){
        if(first_name == null || last_name == null){
            throw new EtAuthException("Unable to register. Invalid parameters passed");
        }else if(first_name == "" && last_name  == ""){
            throw new EtAuthException("Unable to register. Invalid email ID provided");
        }
    }

    /**
     * Verify Payload Parameters (Email ID and Password)
     * @param email
     * @param password
     */
    public void verifyEamilPassPayload(String email, String password){
        if(email == null || password == null){
            throw new EtAuthException("Invalid parameters passed");
        }else if(email  == "" && password == "" ){
            throw new EtAuthException("Invalid email ID provided");
        }
    }

    /**
     * Verify names are in correct format
     * @param first_name
     * @param last_name
     */
    public void verifyNameFormat(String first_name, String last_name){
        if((Pattern.matches("[a-zA-Z ]*",first_name) == false) || (Pattern.matches("[a-zA-Z ]*",last_name) == false)){
            throw new EtAuthException("First name and Last name can only contain letters");
        }else if(first_name.length() < 2 || last_name.length() < 2){
            throw new EtAuthException("First name and Last name should be at least 2 letters");
        }
    }

    /**
     * Verify email is in correct format
     * @param email
     */
    public void verifyEmailFormat(String email){
        if(Pattern.matches("^(.+)@(.+)$",email) == false){
            throw new EtAuthException("Invalid email ID provided");
        }
    }

    /**
     * Generate Username
     * @param firstname
     * @return username
     */
    public String generateUsername(String firstname){
        firstname = firstname.toLowerCase().trim();
        Random rand = new Random();
        int random = rand.nextInt(999 + 1 - 100) + 100;
        char ch1 = firstname.charAt(0);
        char ch2 = firstname.charAt(1);
        return ch1+""+ch2+""+random;
    }


    /**
     * Capitalize Words
     * @param source
     * @return capitalized sentence
     */
    public static String capitalize(String source) {
        source = source.toLowerCase();
        String[] splited = source.split(" ");
        String result = "";

        for(String str : splited){
            result = result + str.substring(0, 1).toUpperCase() + str.substring(1)+" ";
        }
        return result;
    }

}