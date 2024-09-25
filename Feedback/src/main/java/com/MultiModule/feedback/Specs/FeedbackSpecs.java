package com.MultiModule.feedback.Specs;

import com.MultiModule.feedback.Entity.FeedbackEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class FeedbackSpecs {
    public static Specification<FeedbackEntity> contenutoContains(String contenuto) {
        return (root, query, cb) -> cb.like(root.get("contenuto"), "%" + contenuto + "%");
    }

    public static Specification<FeedbackEntity> dataSottomissioneContains(LocalDateTime dataSottomissione) {
        return (feedback, query, cb) -> cb.greaterThanOrEqualTo(feedback.get("dataSottomissione"), dataSottomissione);
    }

    public static Specification<FeedbackEntity> idEquals(Long id) {
        return (feedback, query, cb) -> cb.equal(feedback.get("userid"), id);
    }
}
