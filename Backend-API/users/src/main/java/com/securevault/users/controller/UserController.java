package com.securevault.users.controller;

import com.securevault.users.constants.Constants;
import com.securevault.users.model.Users;
import com.securevault.users.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) {
        Map<String, String> map = new HashMap<>();
        try{
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");
            Users user = userService.validateUser(email, password);
            map.put("status:","success");
            map.put("token:",generateJWTToken(user));
        }catch(Exception e){
            map.put("status:","fail");
            map.put("message:",e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap){
        Map<String, String> map = new HashMap<>();

        try {
            String first_name = (String) userMap.get("first_name");
            String last_name = (String) userMap.get("last_name");
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");
            Users user = userService.registerUser(first_name, last_name, email, password);

            map.put("status:","success");
            map.put("message:","User registered successfully");
        }catch(Exception e) {
            map.put("status:","fail");
            map.put("message:","Unable to register. "+e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    private String generateJWTToken(Users user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .compact();
//        Map<String, String> map = new HashMap<>();
//        map.put("token", token);
//        return map;
        return token;
    }

}
