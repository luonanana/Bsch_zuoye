package com.bestsch.zuoye.service;

import com.bestsch.openapi.BoaUser;
import com.bestsch.zuoye.model.UserZuoye;
import com.bestsch.zuoye.model.ZuoYe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IUserZuoyeService {
    Page<UserZuoye> findAll(UserZuoye userZuoye,  Pageable pageable);
    List<UserZuoye> findAll(UserZuoye userZuoye);

    List<Map<String, Object>> FindZuoyeReview(UserZuoye userZuoye, String page,String count);
    Map<String, Object> FindMyZuoye(BoaUser authUser, UserZuoye userZuoye, ZuoYe zuoYe, String page,String count);
    List<Map<String, Object>> FindClassZuoye(UserZuoye userZuoye);
    List<UserZuoye> findByZuoyeId(Integer qId);
    void deleteAll(List<UserZuoye> userZuoyes);
    List<UserZuoye> findByUserId(Integer userId);
    UserZuoye save(UserZuoye userZuoye);
}
