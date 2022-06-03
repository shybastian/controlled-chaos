package edu.seb.controlled.user.service.impl;

import edu.seb.chaos.now.assault.components.annotation.Targetable;
import edu.seb.controlled.user.entity.User;
import edu.seb.controlled.user.exception.BusinessException;
import edu.seb.controlled.user.repository.UserRepository;
import edu.seb.controlled.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Targetable
@Service
public class UserServiceImpl implements UserService {
    //private final UserRepository userRepository;
    private final List<User> userList;

    public UserServiceImpl() {
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
    public List<User> findAll() {
        return this.userList;
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

    @Override
    public User save(User user) throws BusinessException {
        if (user == null){
            throw new BusinessException("Invalid User!");
        }
        if (user.getUsername() == null) {
            throw new BusinessException("Invalid Username!");
        }
        if (user.getPassword() == null) {
            throw new BusinessException("Invalid Password!");
        }
        if (user.getFirstName() == null) {
            throw new BusinessException("Invalid First Name");
        }
        if (user.getLastName() == null) {
            throw new BusinessException("Invalid Last Name");
        }
//        if (this.userRepository.existsByUsername(user.getUsername())){
//            throw new BusinessException("Username already exists");
//        }
        this.userList.add(user);
        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
