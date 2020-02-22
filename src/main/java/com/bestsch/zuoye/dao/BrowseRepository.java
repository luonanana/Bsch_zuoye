package com.bestsch.zuoye.dao;

import com.bestsch.zuoye.model.Browse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrowseRepository extends JpaRepository<Browse, Integer>, JpaSpecificationExecutor<Browse> {
List<Browse> findByExplainId(Integer qId);
}
