package com.MultiModule.Security.DAO;

import com.MultiModule.Security.Entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDAO extends JpaRepository<RefreshTokenEntity, Long> {
}
