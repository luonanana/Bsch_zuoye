package com.bestsch.zuoye.service.impl;


import com.bestsch.utils.DException;
import com.bestsch.zuoye.dao.ModelTypeRepository;
import com.bestsch.zuoye.dao.ZuoyeModelRepository;
import com.bestsch.zuoye.model.ModelType;
import com.bestsch.zuoye.model.ZuoyeModel;
import com.bestsch.zuoye.service.IModelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelTypeService implements IModelTypeService {

    @Autowired
    private ModelTypeRepository modelTypeRepository;

    @Autowired
    private ZuoyeModelRepository zuoyeModelRepository;
    @Autowired
    private ZuoyeModelService zuoyeModelService;

    @Override
    public void delete(Integer id) {

        List<ZuoyeModel> list = zuoyeModelRepository.findByModelTypeId(id);

        if(list!=null&&list.size()>0){
            throw new DException("分类已被使用,不能删除");
        } else {
            modelTypeRepository.deleteById(id);
        }
    }


    @Override
    public List<ModelType> saveAll(List<ModelType> modelTypes) {

        return modelTypeRepository.saveAll(modelTypes);
    }
    public List<ModelType> getModelTypeTree(){
        List<ModelType> list = modelTypeRepository.findAll();

        List<ModelType> rootModelType = new ArrayList<>();

        if(list!=null&&list.size()!=0){
            for (ModelType modelType :list){
                if(modelType.getPId()==0){
                    rootModelType.add(modelType);
                }
            }
        }

        if(rootModelType.size()!=0){
            for (ModelType modelType :rootModelType ) {
                getTree(modelType,list);
            }
        }
        return  rootModelType;
    }



    public ModelType save(ModelType modelType){

        if(modelType.getId()!=null){
            ModelType modelType2 = modelTypeRepository.findById(modelType.getId()).get();

            if(modelType.getName()!=null){
                modelType2.setName(modelType.getName());
            }
            if(modelType.getPId()!=null){
                modelType2.setPId(modelType.getPId());
            }

            return modelTypeRepository.save(modelType2);
        }
        return modelTypeRepository.save(modelType);
    }





    private void getTree(ModelType modelType ,List<ModelType> list){
        List<ModelType> subList = new ArrayList<>();
        modelType.setChildren(subList);
        Integer modelTypeId = modelType.getId();
        ZuoyeModel zuoyeModel=new ZuoyeModel();
        zuoyeModel.setModelTypeId(modelTypeId);
        List<ZuoyeModel> byModelTypeId = zuoyeModelService.findAll(zuoyeModel);
        modelType.setZuoyeModels(byModelTypeId);
        for (int i = 0; i < list.size(); i++)
        {
            ModelType modelType2 = list.get(i);
            if(modelType.getId()==modelType2.getPId()){
                subList.add(modelType2);
                Integer modelTypeId2 = modelType2.getId();
                ZuoyeModel zuoyeModel2=new ZuoyeModel();
                zuoyeModel2.setModelTypeId(modelTypeId2);
                List<ZuoyeModel> byModelTypeId2 = zuoyeModelService.findAll(zuoyeModel);
                modelType2.setZuoyeModels(byModelTypeId2);
            }
        }

        for (int i = 0; i < subList.size(); i++)
        {
            ModelType modelType2 = subList.get(i);
            getTree(modelType2,list);
        }
    }





}
