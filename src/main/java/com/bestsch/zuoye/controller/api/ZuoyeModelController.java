package com.bestsch.zuoye.controller.api;

import com.bestsch.openapi.BoaUser;
import com.bestsch.utils.DException;
import com.bestsch.utils.ErrorCode;
import com.bestsch.zuoye.config.Config;
import com.bestsch.zuoye.controller.BaseController;
import com.bestsch.zuoye.entity.Page;
import com.bestsch.zuoye.model.ModelQestion;
import com.bestsch.zuoye.model.ZuoyeModel;
import com.bestsch.zuoye.service.IModelQestionService;
import com.bestsch.zuoye.service.IZuoyeModelService;
import com.bestsch.zuoye.service.IZuoyeService;
import com.bestsch.zuoye.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Config.API_PATH)
public class ZuoyeModelController extends BaseController {
    @Autowired
    private IZuoyeModelService zuoyeModelService;
    @Autowired
    private IModelQestionService modelQestionService;
    @Autowired
    private IZuoyeService zuoyeService;

    @GetMapping("FindZuoyeModel")
    public Object findAll(ZuoyeModel zuoyeModel, @Param("page") String page, @Param("count") String count) {
        Page pageVO = PageUtil.getPageVO(page, count);
        Pageable pageable = PageRequest.of(pageVO.getCurrent() - 1, pageVO.getPageSize());
        org.springframework.data.domain.Page<ZuoyeModel> all = zuoyeModelService.findAll(zuoyeModel, pageable);
        List<ZuoyeModel> modelList = all.getContent();
        //作业模版被使用的次数
        for (int i = 0; i <modelList.size() ; i++) {
            ZuoyeModel qm = modelList.get(i);
            Integer useCount= zuoyeService.findByZuoyeModelId(qm.getId()).size();
           qm.setUseCount(useCount);
        }
        return all;
    }
    @PostMapping(value = "SaveZuoyeModel", produces = "application/json")
    public void SaveZuoyeModel(BoaUser authUser, @RequestBody ZuoyeModel zuoyeModel) {
        zuoyeModel.setCId(authUser.getId());
        zuoyeModel.setCName(authUser.getName());
        zuoyeModel.setCDate(new Date());
        zuoyeModelService.save(zuoyeModel);
    }
    @PostMapping("UpdateZuoyeModel")
    public void UpdateZuoyeModel(@RequestBody ZuoyeModel zuoyeModel) {
        zuoyeModelService.save(zuoyeModel);
    }

    @PostMapping("DeleteZuoyeModel")
    public void DeleteZuoye(@RequestParam("id") String idStr) {
        if (StringUtils.isEmpty(idStr) || !StringUtils.isNumeric(idStr))
            throw new DException(String.valueOf(ErrorCode.WRONG_REQUEST_PARAMS));
        Integer id = Integer.parseInt(idStr);
        zuoyeModelService.delete(id);
    }
    //预览查看作业模版
    @GetMapping("GetZuoyeModel")
    public Object getZuoyeModel(ModelQestion modelQestion) {
        Map map=new HashMap();
        try {
            Integer zuoyeModelId = modelQestion.getZuoyeModelId();
            ZuoyeModel zuoyeModel = zuoyeModelService.findById(zuoyeModelId);
            List<ModelQestion> ModelQestion = modelQestionService.findAll(modelQestion);
            map.put("zuoyeModel",zuoyeModel);
            map.put("ModelQestion",ModelQestion);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
