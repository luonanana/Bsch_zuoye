package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.ZuoyeObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ZuoyeObjectRepository extends JpaRepository<ZuoyeObject,Integer>, JpaSpecificationExecutor<ZuoyeObject> {
    List<ZuoyeObject> findByZuoyeId(Integer qId);
    List<ZuoyeObject> findByZuoyeIdIn(List<Integer> qIds);
    @Transactional
    void deleteByZuoyeId(Integer zuoyeId);


}
