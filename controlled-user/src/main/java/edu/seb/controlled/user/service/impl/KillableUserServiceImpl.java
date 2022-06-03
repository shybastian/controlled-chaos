package edu.seb.controlled.user.service.impl;

import edu.seb.chaos.now.assault.components.annotation.Killable;
import edu.seb.controlled.user.entity.User;
import edu.seb.controlled.user.exception.BusinessException;
import edu.seb.controlled.user.service.KillableUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Killable
@Service
@RequiredArgsConstructor
public class KillableUserServiceImpl implements KillableUserService {
    private final List<User> userList;

    public KillableUserServiceImpl() {
        this.userList = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setFirstName("Sebastian");
        user.setLastName("Maier");
        user.setUsername("seb");
        user.setPassword("seb");
        this.userList.add(user);
    }

    @Override
    public User findUserById(Long id) throws BusinessException {
        if (id == null || id == 0){
            throw new BusinessException("Id is incorrect!");
        }
        for (User user : this.userList) {
            if (user.getId().equals(id)) return user;
        }
        return null;
    }
}
