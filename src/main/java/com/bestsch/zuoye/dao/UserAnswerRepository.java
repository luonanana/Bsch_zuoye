package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer>, JpaSpecificationExecutor<UserAnswer> {
List<UserAnswer> findByUserZuoyeId(Integer userZyId);
}
