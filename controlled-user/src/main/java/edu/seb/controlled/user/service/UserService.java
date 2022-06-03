package edu.seb.controlled.user.service;

import edu.seb.controlled.user.entity.User;
import edu.seb.controlled.user.exception.BusinessException;

import java.util.List;


public interface UserService {
    List<User> findAll();
    User findUserById(Long id) throws BusinessException;
    User save(User user) throws BusinessException;
    User update(User user);
    void deleteById(Long id);
}
