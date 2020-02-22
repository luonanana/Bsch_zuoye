package com.bestsch.zuoye.service.impl;
import com.bestsch.zuoye.dao.UserAnswerRepository;
import com.bestsch.zuoye.dao.ZuoyeQestionRepository;
import com.bestsch.zuoye.mem.UserAnswerMem2;
import com.bestsch.zuoye.model.UserAnswer;
import com.bestsch.zuoye.service.IUserAnswerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserAnswerService implements IUserAnswerService {
    @Autowired
    private UserAnswerRepository userAnswerRepository;



    @Autowired
    private UserAnswerMem2 userAnswerMem2;

    @Override
    public List<UserAnswer> findAll(UserAnswer userAnswer) {
        return userAnswerRepository.findAll(this.select(userAnswer));
    }

    @Override
    public UserAnswer findById(Integer id) {
        return userAnswerRepository.findById(id).get();
    }

    @Override
    public void saveAll(List<UserAnswer> userAnswer) {
        userAnswerRepository.saveAll(userAnswer);
    }

    @Override
    public void save(UserAnswer userAnswer) {
        userAnswerRepository.save(userAnswer);
    }

    @Override
    public void deleteAll(List<UserAnswer> userAnswer) {
        userAnswerRepository.deleteAll(userAnswer);
    }

    @Override
    public Object AnswerStatistics(Integer qId) {
        List<Map<String, Object>> objectList = (List<Map<String, Object>>) userAnswerMem2.getByQuesionnaireId(qId);
        if(objectList==null) objectList=new ArrayList<>();
         return objectList;

    }

    @Override
    public Page findAll(UserAnswer userAnswer, Pageable pageable) {
        return userAnswerRepository.findAll(this.select(userAnswer),pageable);
    }

    private Specification select(UserAnswer userAnswer) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (userAnswer != null) {
                    if (userAnswer.getUserZuoyeId() != null) {
                        predicates.add(cb.equal(root.get("userZuoyeId"), userAnswer.getUserZuoyeId()));
                    }
                    if (userAnswer.getZuoyeQestId() != null) {
                        predicates.add(cb.equal(root.get("zuoyeQestId"), userAnswer.getZuoyeQestId()));
                    }
                    if (userAnswer.getAnswer() != null || StringUtils.isNotEmpty(userAnswer.getAnswer())) {
                        predicates.add(cb.like(root.get("answer"), "%" + userAnswer.getAnswer() + "%"));
                    }
                    if (userAnswer.getZuoyeQestion() != null) {
                        if (userAnswer.getZuoyeQestion().getZuoyeId() != null) {
                            predicates.add(cb.equal(root.get("zuoyeQestion").get("zuoyeId"), userAnswer.getZuoyeQestion().getZuoyeId()));
                        }
                        if (userAnswer.getZuoyeQestion().getId() != null) {
                            predicates.add(cb.equal(root.get("zuoyeQestion").get("id"), userAnswer.getZuoyeQestion().getId()));
                        }
                    }
                    if (userAnswer.getUserZuoye() != null) {

                        if (userAnswer.getUserZuoye().getZuoyeId() != null) {
                            predicates.add(cb.equal(root.get("userZuoye").get("zuoyeId"), userAnswer.getUserZuoye().getZuoyeId()));

                        }
                        if (userAnswer.getUserZuoye().getUserId() != null) {
                            predicates.add(cb.equal(root.get("userZuoye").get("userId"), userAnswer.getUserZuoye().getUserId()));
                        }

                    }

                }
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

            }
        };
        return specification;
    }


}
