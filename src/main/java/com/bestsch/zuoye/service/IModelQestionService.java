package com.bestsch.zuoye.service;



import com.bestsch.zuoye.model.ModelQestion;

import java.util.List;

public interface IModelQestionService {
    List<ModelQestion> findAll(ModelQestion modelQestion);
}
