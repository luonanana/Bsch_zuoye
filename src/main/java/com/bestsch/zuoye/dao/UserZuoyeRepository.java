package com.bestsch.zuoye.dao;


import com.bestsch.zuoye.model.UserZuoye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserZuoyeRepository extends JpaRepository<UserZuoye,Integer>, JpaSpecificationExecutor<UserZuoye> {

    List<UserZuoye> findByZuoyeId(Integer zyId);
    List<UserZuoye> findByZuoyeIdAndStatus(Integer zyId,Integer status);
    List<UserZuoye> findByUserId(Integer uId);
    /**
     * 最近一周作业回收情况
     */
    @Query(nativeQuery = true, value = "SELECT date(end_date) AS dateTime,count(*) AS count FROM user_zuoye WHERE end_date>DATE_SUB(CURDATE(), INTERVAL 1 WEEK) and zuoye_id=?1 group by date(end_date);")
    List<Object[]> findRecycleByWeek(Integer qId);

    /**
     * 最近一周作业回收总量
     */
    @Query(nativeQuery = true, value = "SELECT count(*) as total FROM user_zuoye WHERE end_date>DATE_SUB(CURDATE(), INTERVAL 1 WEEK) and zuoye_id=?1")
    Object[] findCountByWeek(Integer qId);

    /**
     * 每天作业回收情况
     */
    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(end_date,'%Y-%m-%d') AS dateTime,COUNT(1) AS count FROM user_zuoye where zuoye_id=?1 GROUP BY DATE_FORMAT(end_date,'%Y-%m-%d');")
    List<Object[]> findRecycleByDay(Integer qId);



}
