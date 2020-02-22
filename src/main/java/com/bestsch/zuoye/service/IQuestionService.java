package com.bestsch.zuoye.service;



import com.bestsch.zuoye.model.Question;

import java.util.List;

public interface IQuestionService {
    List<Question> findAll(Question question);
    void save(Question question);
    void delete(Integer id);
}
