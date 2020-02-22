package com.bestsch.zuoye.service;


import com.bestsch.zuoye.model.ZuoYe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IZuoyeService {
     ZuoYe save(ZuoYe zuoye,List<Integer> classIds,List<Integer> stuIds);
     ZuoYe findById(Integer id);
     void delete(Integer id);
     Page<ZuoYe> findAllByPage(ZuoYe zuoye, Pageable pageable);
     List<ZuoYe> findAll(ZuoYe zuoye,List<Integer> zyIds);
     List<ZuoYe> findByZuoyeModelId(Integer id);
}
