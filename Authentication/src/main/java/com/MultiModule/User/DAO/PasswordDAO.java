package com.MultiModule.User.DAO;

import com.MultiModule.User.Entity.PasswordEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PasswordDAO extends JpaRepository<PasswordEntity, Long> {
    List<PasswordEntity> findByUserId(long id);

    List<PasswordEntity> findTop10ByUserIdOrderByDataCreazioneDesc(long id);

    @Query("SELECT p.id FROM PasswordEntity p WHERE p.userId = :userId ORDER BY p.dataCreazione DESC")
    List<Long> findTop10ByUserIdOrderByDataCreazioneDesc(@Param("userId") long id, Pageable pageable);

    PasswordEntity findFirstByUserIdOrderByDataCreazioneDesc(long id);

    @Modifying
    @Query("DELETE FROM PasswordEntity p WHERE p.userId = :userId AND p.id NOT IN (:passIds)")
    void deleteOldPasswords(@Param("userId") long userId, @Param("passIds") List<Long> passIds);

}

