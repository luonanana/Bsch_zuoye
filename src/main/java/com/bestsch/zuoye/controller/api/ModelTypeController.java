package com.bestsch.zuoye.controller.api;


import com.bestsch.utils.DException;
import com.bestsch.utils.ErrorCode;
import com.bestsch.zuoye.config.Config;
import com.bestsch.zuoye.model.ModelType;
import com.bestsch.zuoye.service.IModelTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Config.API_PATH)
public class ModelTypeController {

    @Autowired
    private IModelTypeService modelTypeService;


    @GetMapping("GetModelTypeTree")
    public List<ModelType> findAll(){
        return modelTypeService.getModelTypeTree();
    }

    @PostMapping("SaveModelType")
    public ModelType SaveMOdelType(ModelType modelType){
        return modelTypeService.save(modelType);
    }

    @PostMapping("RemoveModelType")
    public List<ModelType> SaveAllModelType(@RequestBody List<ModelType> modelTypes){
        return modelTypeService.saveAll(modelTypes);
    }
    @PostMapping("DeleteModelType")
    public void delete(@RequestParam("id")String id){
        if (StringUtils.isEmpty(id)||!StringUtils.isNumeric(id)) {
            new DException(ErrorCode.WRONG_REQUEST_PARAMS);
        }
        modelTypeService.delete(Integer.parseInt(id));
    }
}
