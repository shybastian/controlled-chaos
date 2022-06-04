package edu.seb.controlled.user.controller;

import edu.seb.controlled.user.entity.User;
import edu.seb.controlled.user.exception.BusinessException;
import edu.seb.controlled.user.mapper.UserMapper;
import edu.seb.controlled.user.mapper.to.UserTO;
import edu.seb.controlled.user.service.KillableUserService;
import edu.seb.controlled.user.service.UserService;
import edu.seb.controlled.user.service.impl.KillableUserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class UserController {
    private final UserService userService;
    private final KillableUserService killableUserService;

    private final UserMapper userMapper;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        try {
            log.info("Received GET Reqs with id: {}", id);
            UserTO to = this.userMapper.toTO(this.userService.findUserById(id));
            return ResponseEntity.ok(to);
        } catch (BusinessException exception) {
            return ResponseEntity.badRequest().body(exception);
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception);
        }
    }

    @GetMapping(value ="/{id}/kill", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserByIdKill(@PathVariable Long id) {
        try {
            log.info("Received Kill request.");
            this.killableUserService.findUserById(id);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Kill didn't work.");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserTO> getAll() {
        final List<UserTO> userTOList = new ArrayList<>();
        final List<User> userList = this.userService.findAll();
        for (User user : userList) {
            userTOList.add(this.userMapper.toTO(user));
        }
        return userTOList;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertUser(@RequestBody UserTO userTO) {
        try {
            if (userTO != null) {
                return ResponseEntity.ok(this.userMapper.toTO(this.userService.save(this.userMapper.fromTO(userTO))));
            }
        } catch (BusinessException exception) {
            return ResponseEntity.badRequest().body(exception);
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception);
        }
        return null;
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteEverything() {
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }


}
