package com.bestsch.zuoye.service.impl;

import com.bestsch.zuoye.dao.ModelQestionRepository;
import com.bestsch.zuoye.model.ModelQestion;
import com.bestsch.zuoye.service.IModelQestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelQestionService implements IModelQestionService {
    @Autowired
    private ModelQestionRepository modelQestionRepository;
    @Override
    public List<ModelQestion> findAll(ModelQestion modelQestion) {
        return modelQestionRepository.findAll(this.select(modelQestion));
    }
    private Specification<ModelQestion> select(ModelQestion modelQestion) {
        Specification<ModelQestion> queryCondition = new Specification<ModelQestion>() {
            @Override
            public Predicate toPredicate(Root<ModelQestion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (modelQestion != null) {
                    if (modelQestion.getZuoyeModel() != null && modelQestion.getZuoyeModel().getModelTypeId() != null) {
                        predicates.add(cb.equal(root.get("modelQestion").get("modelTypeId"), modelQestion.getZuoyeModel().getModelTypeId()));
                    }
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return queryCondition;
    }
}
