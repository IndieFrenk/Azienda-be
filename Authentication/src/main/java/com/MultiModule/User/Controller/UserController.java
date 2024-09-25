package com.MultiModule.User.Controller;

import com.MultiModule.User.Entity.UserEntity;
import com.MultiModule.User.DAO.UserDAO;
import com.MultiModule.User.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        @GetMapping("/me")
        public ResponseEntity<UserEntity> authenticatedUser() {
            return userService.authenticatedUser();
        }

        @GetMapping("/")
        public ResponseEntity<List<UserEntity>> allUsers() {
            return userService.allUsers();
        }
    }


