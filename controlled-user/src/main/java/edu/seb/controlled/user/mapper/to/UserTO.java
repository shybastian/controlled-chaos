package edu.seb.controlled.user.mapper.to;

import lombok.*;

@Data
public class UserTO {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
