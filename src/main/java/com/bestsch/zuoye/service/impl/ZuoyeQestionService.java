package com.bestsch.zuoye.service.impl;

import com.bestsch.zuoye.dao.ZuoyeQestionRepository;
import com.bestsch.zuoye.model.ZuoyeQestion;
import com.bestsch.zuoye.service.IZuoyeQestionService;
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
public class ZuoyeQestionService implements IZuoyeQestionService {
   @Autowired
   private ZuoyeQestionRepository zuoyeQestionRepository;
    @Override
    public List<ZuoyeQestion> findAll(ZuoyeQestion zuoyeQestion, List<Integer> questTypeId) {
        return zuoyeQestionRepository.findAll(this.select(zuoyeQestion,questTypeId));
    }

    @Override
    public List<ZuoyeQestion> findByZuoyeId(Integer qId) {
        return zuoyeQestionRepository.findByZuoyeId(qId);
    }

    @Override
    public List<ZuoyeQestion> saveAll(List<ZuoyeQestion> zuoyeQestions) {
        return zuoyeQestionRepository.saveAll(zuoyeQestions);
    }

    private Specification<ZuoyeQestion> select(ZuoyeQestion zuoyeQestion, List<Integer> questTypeIds) {
        Specification<ZuoyeQestion> queryCondition = new Specification<ZuoyeQestion>() {
            @Override
            public Predicate toPredicate(Root<ZuoyeQestion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (zuoyeQestion != null) {
                    if (zuoyeQestion.getZuoyeId() != null) {
                           predicates.add(cb.equal(root.get("zuoyeId"), zuoyeQestion.getZuoyeId()));

                       }
                }
                if(questTypeIds!=null&&questTypeIds.size()>0){
                    CriteriaBuilder.In<Object> in = cb.in(root.get("questionTypeId"));
                    for (Integer id : questTypeIds) {
                        in.value(id);
                    }
                    predicates.add(in);
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return queryCondition;
    }

}
