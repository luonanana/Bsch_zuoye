package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.ZuoYe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ZuoyeRepository extends JpaRepository<ZuoYe, Integer>, JpaSpecificationExecutor<ZuoYe> {


    List<ZuoYe> findByZuoyeModelId(Integer id);

}
