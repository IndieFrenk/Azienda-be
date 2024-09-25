package com.MultiModule.feedback.DAO;

import com.MultiModule.feedback.Entity.FeedbackEntity;
import com.MultiModule.feedback.Entity.RispostaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RispostaDAO extends JpaRepository<RispostaEntity, Long> {
    List<RispostaEntity> findByFeedback(FeedbackEntity feedback);
}
