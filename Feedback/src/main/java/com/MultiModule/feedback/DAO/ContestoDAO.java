package com.MultiModule.feedback.DAO;

import com.MultiModule.feedback.Entity.ContestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface ContestoDAO  extends JpaRepository<ContestoEntity, Long> {
    ContestoEntity findByDefinizione(String definizione);
}
