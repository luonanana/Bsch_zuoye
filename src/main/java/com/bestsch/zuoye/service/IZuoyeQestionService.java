package com.bestsch.zuoye.service;



import com.bestsch.zuoye.model.ZuoyeQestion;

import java.util.List;

public interface IZuoyeQestionService {
    List<ZuoyeQestion> findAll(ZuoyeQestion zuoyeQestion, List<Integer> questTypeId);
    List<ZuoyeQestion> findByZuoyeId(Integer qId);
    List<ZuoyeQestion> saveAll(List<ZuoyeQestion> zuoyeQestions);
}
