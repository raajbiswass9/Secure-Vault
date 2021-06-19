package com.securevault.users.controller;

import com.securevault.users.constants.Constants;
import com.securevault.users.model.Users;
import com.securevault.users.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/dashboard")
    public String getDashboard(HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("userId");
        return "Hello user. ID: "+userId;
    }

    @GetMapping("/all")
    public String all() {
//        int userId = (Integer) request.getAttribute("userId");
        return "Hello All ";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        Users user = userService.validateUser(email, password);
        ;
//        Map<String, String> map = new HashMap<>();
//        map.put("Message:","User loggedin succesfully");
//        map.put("UserDetails","Ok");

//        return new ResponseEntity<>(map, HttpStatus.OK);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap){
        String first_name = (String) userMap.get("first_name");
        String last_name = (String) userMap.get("last_name");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        Users user = userService.registerUser(first_name, last_name, email, password);

        Map<String, String> map = new HashMap<>();
        map.put("Message:","all ok");
//        map.put("UserName",user.getUsername());

        return new ResponseEntity<>(map, HttpStatus.OK);
//        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }


    private Map<String, String> generateJWTToken(Users user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

}
