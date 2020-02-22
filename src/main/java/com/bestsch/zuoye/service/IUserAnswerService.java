package com.bestsch.zuoye.service;


import com.bestsch.zuoye.model.UserAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserAnswerService {
    List<UserAnswer> findAll(UserAnswer userAnswer);
    UserAnswer findById(Integer id);
    void saveAll(List<UserAnswer> userAnswer);
    void save(UserAnswer userAnswer);
    void deleteAll(List<UserAnswer> userAnswer);
    Object AnswerStatistics(Integer qId);
    Page findAll(UserAnswer userAnswer, Pageable pageable) ;
}
