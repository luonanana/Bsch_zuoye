package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelTypeRepository extends JpaRepository<ModelType, Integer>, JpaSpecificationExecutor<ModelType> {

}
