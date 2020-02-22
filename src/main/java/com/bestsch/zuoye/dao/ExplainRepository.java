package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.Explain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ExplainRepository extends JpaRepository<Explain, Integer>, JpaSpecificationExecutor<Explain> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete  from  explain where id in ?1)")
    void deleteAll(List<Integer> ids);
}
