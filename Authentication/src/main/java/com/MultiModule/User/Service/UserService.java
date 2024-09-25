package com.MultiModule.User.Service;

import com.MultiModule.User.DAO.PasswordDAO;
import com.MultiModule.User.DAO.RoleDAO;
import com.MultiModule.User.DAO.UserDAO;
import com.MultiModule.User.Entity.PasswordEntity;
import com.MultiModule.User.Entity.Privilege;
import com.MultiModule.User.Entity.RoleEntity;
import com.MultiModule.User.Entity.UserEntity;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private PasswordDAO passwordDAO;


    public UserService(UserDAO userRepository) {
        this.userDAO = userRepository;
    }

    public ResponseEntity<UserEntity> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        val currentUser = (UserEntity) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    public ResponseEntity<List<UserEntity>> allUsers() {
        List<UserEntity> users = new ArrayList<>();
        userDAO.findAll().forEach(users::add);

        return ResponseEntity.ok(users);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userDAO.findByEmail(email);
        return  user;

    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<RoleEntity> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<RoleEntity> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
    public void initRoleAndUser() {
    if (roleDAO.findByName("Admin") == null)  {
            RoleEntity adminRole = new RoleEntity();
            adminRole.setName("Admin");
            roleDAO.save(adminRole);

            RoleEntity userRole = new RoleEntity();
            userRole.setName("User");
            roleDAO.save(userRole);
            //pattern builder
            if (userDAO.findByUsername("admin").isEmpty()) {
                UserEntity adminUser = new UserEntity();
                adminUser.setUsername("admin");
                var password = (passwordEncoder().encode("admin@pass"));
                adminUser.setPassword(password);
                adminUser.setNome("admin");
                adminUser.setCognome("admin");
                adminUser.setEmail("admin@mail");
                Set<RoleEntity> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                adminUser.setRoles(adminRoles);
                userDAO.save(adminUser);
                passwordMemorization(adminUser);
            }
        }
    }
    public void passwordMemorization (UserEntity User){

        var password =  new PasswordEntity(User.getPassword(),LocalDateTime.now(), User.getId());
        passwordDAO.save(password);
    }

    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
