package com.MultiModule.feedback.DAO;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.function.Predicate;

public interface Specs<T>{
    Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb);

}
