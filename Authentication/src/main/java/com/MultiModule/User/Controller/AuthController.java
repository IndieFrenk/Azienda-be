package com.MultiModule.User.Controller;

import com.MultiModule.User.DTO.ChangePassDTO;
import com.MultiModule.User.DTO.LoginUserDTO;
import com.MultiModule.User.DTO.RegisterUserDTO;
import com.MultiModule.User.Entity.UserEntity;
import com.MultiModule.User.Service.AuthenticationService;
import com.MultiModule.User.Service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    @Autowired

    public AuthController( AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;
    }
    @PostConstruct
    public void initRoleAndUser() {
        //userService.initRoleAndUser();
    }
    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserDTO registerUserDto) throws Exception {
        var registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/changePass")
    public ResponseEntity<?> changePass(@RequestBody ChangePassDTO changePassDTO) throws Exception {
        return ResponseEntity.ok( authenticationService.changePass(changePassDTO));
    }
    @PostMapping("/login")
    public ResponseEntity<?>  authenticate(@RequestBody LoginUserDTO loginUserDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginUserDto));

    }
    @PostMapping("/recoverPass")
    public ResponseEntity<?>  recoverPass(@RequestBody LoginUserDTO loginUserDto) throws MessagingException, IOException {
        return ResponseEntity.ok(authenticationService.recoverPass(loginUserDto));

    }
    @GetMapping("/get")
    public List<UserEntity> getUsers(){
        return authenticationService.findAll();
    }
    @GetMapping("/get/{id}")
    public Optional<Optional<UserEntity>> getUsersByID(@PathVariable Long id){
        return authenticationService.findById(id);
    }
}
