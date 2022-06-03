package edu.seb.controlled.core.mapper.io;


import lombok.Data;

@Data
public class UserTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
