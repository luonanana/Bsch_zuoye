package com.bestsch.zuoye.service;


import com.bestsch.zuoye.entity.UserClass;
import com.bestsch.zuoye.model.ZuoYe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IZuoyeService {
     ZuoYe save(ZuoYe zuoye);
     ZuoYe save(ZuoYe zuoye, List<UserClass> userClasses);
     ZuoYe findById(Integer id);
     void delete(Integer id);
     Page<ZuoYe> findAllByPage(ZuoYe zuoye, Pageable pageable);
     List<ZuoYe> findAll(ZuoYe zuoye,List<Integer> zyIds);
     List<ZuoYe> findByZuoyeModelId(Integer id);
}
