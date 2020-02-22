package com.bestsch.zuoye.service.impl;

import com.bestsch.utils.DException;
import com.bestsch.zuoye.dao.ModelQestionRepository;
import com.bestsch.zuoye.dao.ModelTypeRepository;
import com.bestsch.zuoye.dao.ZuoyeModelRepository;
import com.bestsch.zuoye.dao.ZuoyeRepository;
import com.bestsch.zuoye.model.ModelQestion;
import com.bestsch.zuoye.model.ModelType;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeModel;
import com.bestsch.zuoye.service.IZuoyeModelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZuoyeModelService implements IZuoyeModelService {
    @Autowired
    private ZuoyeModelRepository zuoyeModelRepository;
    @Autowired
    private ZuoyeRepository zuoyeRepository;
    @Autowired
    private ModelQestionRepository modelQestionRepository;
    @Autowired
    private ModelTypeRepository modelTypeRepository;

    @Override
    public List<ZuoyeModel> findAll(ZuoyeModel zuoyeModel) {
        return zuoyeModelRepository.findAll(this.select(zuoyeModel));
    }

    @Override
    public Page<ZuoyeModel> findAll(ZuoyeModel zuoyeModel, Pageable pageable) {
      return zuoyeModelRepository.findAll(this.select(zuoyeModel),pageable);

    }
    private List<Integer> getSubChild(ModelType modelType, List<Integer> ids) {
        ModelType tree = getChildren(modelType);
        ids.add(tree.getId());
        List<ModelType> children = tree.getChildren();
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                ModelType modelType2 = children.get(i);
                Integer id = modelType2.getId();
                ids.add(id);
                getSubChild(modelType2,ids);
            }
        }
        return ids;
    }
    private ModelType getChildren(ModelType modelType){
        List<ModelType> list = modelTypeRepository.findAll();
        List<ModelType> subList = new ArrayList<>();
        modelType.setChildren(subList);
        for (int i = 0; i < list.size(); i++)
        {
            ModelType modelType2 = list.get(i);
            if(modelType.getId()==modelType2.getPId()){
                subList.add(modelType2);
            }
        }

        for (int i = 0; i < subList.size(); i++)
        {
            ModelType modelType2 = subList.get(i);
            getChildren(modelType2);
        }
        return modelType;
    }

    @Override
    public void save(ZuoyeModel zuoyeModel) {
        //1.先存作业模版
        zuoyeModel = zuoyeModelRepository.save(zuoyeModel);
        Integer modelId = zuoyeModel.getId();
        if(modelId!=null) {
            //2.如果是修改 则删除作业模版题目
            List<ModelQestion> modelQestions = modelQestionRepository.findByZuoyeModelId(modelId);
            if (modelQestions != null & modelQestions.size() > 0)
                modelQestionRepository.deleteAll(modelQestions);
        }
        List<ModelQestion> qestionList = zuoyeModel.getZuoyeQestionList();
        if (qestionList != null && qestionList.size() > 0) {
            Integer id = zuoyeModel.getId();
            for (ModelQestion modelQestion : qestionList) {
                modelQestion.setZuoyeModelId(id);
                //2.存作业模版题目
                modelQestionRepository.saveAll(qestionList);
            }

        }

    }

    @Override
    public void delete(Integer id) {
        List<ZuoYe> zuoyes = zuoyeRepository.findByZuoyeModelId(id);
        if(zuoyes!=null&&zuoyes.size()>0){
            throw new DException("模版类型和作业有关联,不能删除");
        }
        List<ModelQestion> zuoyeQestions = modelQestionRepository.findByZuoyeModelId(id);
        //1.删除作业模版题目
        if (zuoyeQestions != null & zuoyeQestions.size() > 0)
            modelQestionRepository.deleteAll(zuoyeQestions);
        //2.删除作业模版
        zuoyeModelRepository.deleteById(id);
    }

    @Override
    public ZuoyeModel findById(Integer id) {
        return zuoyeModelRepository.findById(id).get();
    }
    private Specification select(ZuoyeModel zuoyeModel) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (zuoyeModel != null) {
                    if (StringUtils.isNotEmpty(zuoyeModel.getTitle())) {

                        Predicate p1 = cb.like(root.get("title"), "%" + zuoyeModel.getTitle() + "%");
                        Predicate p2 = cb.like(root.get("cName"), "%" + zuoyeModel.getCName() + "%");


                        predicates.add(cb.or(p1, p2));
                    }
                    if (zuoyeModel.getModelTypeId() != null) {
                        ModelType modelType = new ModelType();
                        modelType.setId(zuoyeModel.getModelTypeId());
                        List<Integer> ids = new ArrayList<>();
                        ids = getSubChild(modelType, ids);
                        if (ids != null && ids.size() > 0) {
                            CriteriaBuilder.In in = cb.in(root.get("modelTypeId"));
                            for (int i = 0; i < ids.size(); i++) {
                                in.value(ids.get(i));
                            }
                            predicates.add(in);
                        }
                    }
                }

                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }
        };
        return specification;

}

    }
