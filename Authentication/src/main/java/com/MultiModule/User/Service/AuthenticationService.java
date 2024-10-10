package com.MultiModule.User.Service;

import com.MultiModule.User.DAO.PasswordDAO;
import com.MultiModule.User.DAO.RoleDAO;
import com.MultiModule.User.DAO.UserDAO;
import com.MultiModule.User.DTO.ChangePassDTO;
import com.MultiModule.User.DTO.LoginUserDTO;
import com.MultiModule.User.DTO.RegisterUserDTO;
import com.MultiModule.User.Entity.PasswordEntity;
import com.MultiModule.User.Entity.RoleEntity;
import com.MultiModule.User.Entity.UserEntity;
import com.MultiModule.User.Utils.LoginResponse;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthenticationService {

    @Value("${app.max.password-saved}")
    private int passwordSaved;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private final UserDAO userRepository;

    private final RoleDAO roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordDAO passRepository;

    private AuthenticationManager authenticationManager;

    private EmailService emailService;


    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    public AuthenticationService( EmailService emailService,
             UserDAO userRepository, RoleDAO roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, PasswordDAO passRepository
    ) {
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passRepository = passRepository;
    }
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }
    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 12);
    }

    @Transactional
    public UserEntity signup(RegisterUserDTO input) throws Exception { //migliorare la scrittura

        if (emailExist(input.getEmail())) {
            throw new BadCredentialsException
                    ("There is an account with that email address: " + input.getEmail());
        }
        String tempPassword = generateTempPassword();
        String resetToken = generateResetToken();
        UserEntity user = new UserEntity();

        String passEncoded = passwordEncoder.encode(tempPassword);

        user.setFirstLogin(true);
        user.setNome(input.getNome());
        user.setPassword(passEncoded);
        user.setResetPasswordToken(resetToken);
        user.setEmail(input.getEmail());
        user.setUsername(input.getUsername());
        user.setDataDiNascita(LocalDateTime.parse(input.getDataDiNascita().substring(0,input.getDataDiNascita().length()-2)));
        user.setCognome(input.getCognome());

        user.setScadenzaResetToken(LocalDateTime.now().plusMinutes(15));
        Set<RoleEntity> userRoles = new HashSet<>();
        var userRole = roleRepository.findByName("User");
        userRoles.add(userRole);
        user.setRoles(userRoles);
        var userTemp = userRepository.save(user);
        passwordMemorization(userTemp);
        emailService.sendTempPasswordEmail(user.getEmail(),tempPassword,resetToken);
        return userTemp;
    }

    @Transactional
    public boolean recoverPass(LoginUserDTO loginUserDTO) throws MessagingException, IOException {
        UserEntity user = userRepository.findByEmail(loginUserDTO.getEmail());
        String resetToken = generateResetToken();
        user.setResetPasswordToken(resetToken);
        user.setScadenzaResetToken(LocalDateTime.now().plusHours(1));
        user.setFirstLogin(true);
        userRepository.save(user);
        emailService.sendPasswordResetEmail(loginUserDTO.getEmail(),resetToken);

        return true;
    }
    private boolean emailExist(String email) {
        if ( userRepository.findByEmail(email) != null ) {
            return true;
        }
        else { return false;}
    }

    public String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return "No user authenticated";
    }

    public LoginResponse authenticate(LoginUserDTO input) {

        if (LocalDateTime.now().isBefore(passRepository.findFirstByUserIdOrderByDataCreazioneDesc(userRepository.findByEmail(input.getEmail()).getId()).getDataCreazione().plusMonths(1))){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );}
        else {
           throw new BadCredentialsException("Credenziali errate");
        }
        UserEntity authenticatedUser = userRepository.findByEmail(input.getEmail());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(authenticatedUser.getId());
        loginResponse.setEmail(authenticatedUser.getEmail());
        return loginResponse;
    }

    @Transactional
    public LoginUserDTO changePass(ChangePassDTO input) throws BadRequestException {
        System.out.println(input);
        long userId;
        UserEntity oldUser;
        if( userRepository.findByResetPasswordToken(input.getResetToken()).get().isFirstLogin()) {
            if (userRepository.findByResetPasswordToken(input.getResetToken()).isEmpty() ){
                throw new BadCredentialsException("Errore con il token");
            }
            UserEntity tempUser = userRepository.findByResetPasswordToken(input.getResetToken()).get();
            if(tempUser.getScadenzaResetToken().isBefore(LocalDateTime.now())) {
                throw new BadCredentialsException("Token scaduto");
            }
            oldUser = userRepository.findByResetPasswordToken(input.getResetToken()).get();
            userId=(userRepository.findByResetPasswordToken(input.getResetToken()).get().getId());
        }
        else {
            if (!passwordEncoder.matches(input.getPassword(), userRepository.findByEmail(input.getEmail()).getPassword())) {
                throw new BadCredentialsException("Password errata");
            }
            userId = (userRepository.findByEmail(input.getEmail()).getId());
             oldUser = userRepository.findByEmail(input.getEmail());
        }

        List<PasswordEntity> Pass = passRepository.findTop10ByUserIdOrderByDataCreazioneDesc(userId);
        Pageable pageable = PageRequest.of(0, passwordSaved, Sort.by(Sort.Direction.DESC, "dataCreazione"));
        List<Long> oldIndexList =  passRepository.findTop10ByUserIdOrderByDataCreazioneDesc(userId, pageable);
        passRepository.deleteOldPasswords(userId,oldIndexList);
        var newCredentials = new LoginUserDTO();
        System.out.println(Pass.toString());
        for (PasswordEntity i:Pass) {
            if (passwordEncoder.matches(input.getNewPassword(), i.getPassword())) {
                throw new BadRequestException("Password già utilizzata");
            }
        }

        String newPass = passwordEncoder.encode(input.getNewPassword());

        newCredentials.setPassword(newPass);
        newCredentials.setEmail(input.getEmail());

        var loginUserDTO = new LoginUserDTO();

        loginUserDTO.setPassword(newPass); loginUserDTO.setEmail(input.getEmail());


        passRepository.save(new PasswordEntity(newPass, LocalDateTime.now(), userId ));
        oldUser.setPassword(newPass);
        String resetToken = generateResetToken();
        oldUser.setResetPasswordToken(resetToken);
        newCredentials.setResetToken(resetToken);
        oldUser.setFirstLogin(false);
        userRepository.save(oldUser);
        return newCredentials;
    }



    public List<UserEntity> findAll() {

        return (List<UserEntity>) userRepository.findAll();
    }

    public Optional<Optional<UserEntity>> findById(Long Id) {

        return Optional.of(userRepository.findById(Id));
    }
    @Transactional
    public void passwordMemorization (UserEntity User){

        var password =  new PasswordEntity(User.getPassword(),LocalDateTime.now(), User.getId());
        passRepository.save(password);
    }

    public List<PasswordEntity> retrievePassword (long UserId){
        return passRepository.findByUserId(UserId);
    }
    public UserEntity currentUser(){
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
}

