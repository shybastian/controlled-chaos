package edu.seb.controlled.user.service;

import edu.seb.controlled.user.entity.User;
import edu.seb.controlled.user.exception.BusinessException;

public interface KillableUserService {
    User findUserById(Long id) throws BusinessException;
}
