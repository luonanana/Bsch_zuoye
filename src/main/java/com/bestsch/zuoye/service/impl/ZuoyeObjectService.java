package com.bestsch.zuoye.service.impl;

import com.bestsch.openapi.BoaGroup;
import com.bestsch.openapi.BoaUser;
import com.bestsch.openapi.BschBaseOpenApi;
import com.bestsch.zuoye.dao.UserZuoyeRepository;
import com.bestsch.zuoye.dao.ZuoyeObjectRepository;
import com.bestsch.zuoye.enums.BaseSysRole;
import com.bestsch.zuoye.enums.ZuoyeType;
import com.bestsch.zuoye.model.ModelQestion;
import com.bestsch.zuoye.model.UserZuoye;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeObject;
import com.bestsch.zuoye.service.IZuoyeObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class ZuoyeObjectService implements IZuoyeObjectService {

    @Autowired
    private ZuoyeObjectRepository zuoyeObjectRepository;




    @Override
    public void saveAll(List<ZuoyeObject> zuoyeObject) {

        zuoyeObjectRepository.saveAll(zuoyeObject);
    }

    @Override
    public List<ZuoyeObject> findAll(ZuoyeObject zuoyeObject) {

        return zuoyeObjectRepository.findAll(select(zuoyeObject));
    }

    private Specification select(ZuoyeObject zuoyeObject) {
        Specification<ModelQestion> queryCondition = new Specification<ModelQestion>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<ModelQestion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<javax.persistence.criteria.Predicate> predicates = new ArrayList<javax.persistence.criteria.Predicate>();
                if (zuoyeObject != null) {
                    if (zuoyeObject.getClaId() != null) {
                        predicates.add(cb.equal(root.get("claId"), zuoyeObject.getClaId()));
                    }
                    if (zuoyeObject.getStuId() != null) {
                        predicates.add(cb.equal(root.get("stuId"), zuoyeObject.getStuId()));
                    }
                }

                return cb.and(predicates.toArray(new javax.persistence.criteria.Predicate[predicates.size()]));
            }
        };
        return queryCondition;
    }


}
