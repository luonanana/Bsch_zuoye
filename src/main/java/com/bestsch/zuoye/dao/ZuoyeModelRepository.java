package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.ZuoyeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZuoyeModelRepository extends JpaRepository<ZuoyeModel,Integer>, JpaSpecificationExecutor<ZuoyeModel> {

    List<ZuoyeModel> findByModelTypeId(Integer modelTypeId);
}
