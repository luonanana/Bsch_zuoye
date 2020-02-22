package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.ModelQestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelQestionRepository extends JpaRepository<ModelQestion,Integer>, JpaSpecificationExecutor<ModelQestion> {
List<ModelQestion> findByZuoyeModelId(Integer id);
}
