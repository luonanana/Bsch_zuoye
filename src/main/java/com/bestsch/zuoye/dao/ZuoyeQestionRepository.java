package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.ZuoyeQestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZuoyeQestionRepository extends JpaRepository<ZuoyeQestion,Integer>, JpaSpecificationExecutor<ZuoyeQestion> {
List<ZuoyeQestion> findByZuoyeId(Integer qId);
}
