package com.securevault.users.model;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @NotBlank(message="First name can not be empty")
    @Column(nullable = false, length = 40)
    private String first_name;

    @NotBlank(message="Last name can not be empty")
    @Column(nullable = false, length = 40)
    private String last_name;

    @NotBlank(message="Username can not be empty")
    @Column(nullable = false, length = 8)
    private String username;

    @NotBlank(message="Email ID can not be empty")
    @Column(nullable = false)
    private String email;

    @NotBlank(message="Password can not be empty")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active_status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive_status() {
        return active_status;
    }

    public void setActive_status(boolean active_status) {
        this.active_status = active_status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @CreationTimestamp
    @Column(nullable = false)
    private Date created_at;

    public Users(String first_name, String last_name, String username, String email, String password, boolean active_status) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active_status = active_status;
    }


    public Users() {
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + first_name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}