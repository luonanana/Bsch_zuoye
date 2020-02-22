package com.bestsch.zuoye.service;


import com.bestsch.zuoye.model.ZuoyeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IZuoyeModelService {
    List<ZuoyeModel> findAll(ZuoyeModel zuoyeModel);
    Page<ZuoyeModel> findAll(ZuoyeModel zuoyeModel, Pageable pageable);
void save(ZuoyeModel zuoyeModel);
void  delete(Integer id);
    ZuoyeModel findById(Integer id);
}
