package com.bestsch.zuoye.service;



import com.bestsch.openapi.BoaUser;
import com.bestsch.zuoye.model.Explain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface IExplainService {
    Page<Explain> findAll(Explain explain, Pageable pageable);
    List<Explain> FindMyExplain(BoaUser boaUser, Explain explain);
    List<Explain> findAll(Explain explain);
    void save(Explain explain);
    void deleteAll(List<Integer> ids);
    Explain findById(Integer id);
}
