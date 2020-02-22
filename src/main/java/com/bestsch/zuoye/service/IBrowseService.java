package com.bestsch.zuoye.service;



import com.bestsch.zuoye.model.Browse;

import java.util.List;

public interface IBrowseService {
    void save(Browse browse);
    List<Browse> findByExplainId(Integer expId);
}
