package com.MultiModule.User.DAO;

import com.MultiModule.User.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<UserEntity, Long> {
    List<Optional<UserEntity>> findByUsername(String username);
    Optional<UserEntity> findByResetPasswordToken(String token);
    UserEntity findByEmail(String email);
}
