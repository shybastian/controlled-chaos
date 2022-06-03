package edu.seb.controlled.core.entity;


import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
