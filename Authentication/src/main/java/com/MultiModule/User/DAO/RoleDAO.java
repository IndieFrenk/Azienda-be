package com.MultiModule.User.DAO;

import com.MultiModule.User.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String roleUser);
}
