package com.bestsch.zuoye.service.impl;

import com.bestsch.zuoye.dao.QuestionRepository;
import com.bestsch.zuoye.model.Question;
import com.bestsch.zuoye.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService implements IQuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Override
    public List<Question> findAll(Question question) {
        return questionRepository.findAll((root,query,cb)->{
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (question != null) {
                if (question.getQuestionTypeId() != null) {
                    predicates.add(cb.equal(root.get("questionTypeId"), question.getQuestionTypeId()));
                }

            }
             return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

        });
    }

    @Override
    public void save(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void delete(Integer id) {
        questionRepository.deleteById(id);
    }
}
