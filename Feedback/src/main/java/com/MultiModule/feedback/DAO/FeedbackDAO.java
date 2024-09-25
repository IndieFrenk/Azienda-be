package com.MultiModule.feedback.DAO;

import com.MultiModule.feedback.Entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackDAO extends JpaRepository<FeedbackEntity, Long>, JpaSpecificationExecutor<FeedbackEntity> {
    List<FeedbackEntity> findByUserid(long id);
    Optional<FeedbackEntity> findById(Long id);


}
