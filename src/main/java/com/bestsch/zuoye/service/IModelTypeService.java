package com.bestsch.zuoye.service;



import com.bestsch.zuoye.model.ModelType;

import java.util.List;

public interface IModelTypeService {
     List<ModelType> getModelTypeTree();
     void delete(Integer id);
     ModelType save(ModelType modelType);
     List<ModelType> saveAll(List<ModelType> modelTypes);

}
