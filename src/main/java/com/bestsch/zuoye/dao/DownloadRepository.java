package com.bestsch.zuoye.dao;

import com.bestsch.zuoye.model.Download;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadRepository extends JpaRepository<Download, Integer>, JpaSpecificationExecutor<Download> {
List<Download> findByExplainId(Integer qId);
}
