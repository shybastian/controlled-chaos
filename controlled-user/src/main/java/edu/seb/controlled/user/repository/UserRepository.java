package edu.seb.controlled.user.repository;

import edu.seb.controlled.user.entity.User;
public interface UserRepository {
    boolean existsByUsername(String username);
}
