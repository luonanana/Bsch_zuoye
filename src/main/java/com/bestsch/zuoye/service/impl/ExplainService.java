package com.bestsch.zuoye.service.impl;

import com.bestsch.openapi.BoaUser;
import com.bestsch.zuoye.dao.ExplainRepository;
import com.bestsch.zuoye.model.Explain;

import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.service.IExplainService;
import com.bestsch.zuoye.model.Explain;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExplainService implements IExplainService {
    @Autowired
    private ExplainRepository explainRepository;
    @Autowired
    private ClassService classService;

    @Override
    public Page<Explain> findAll(Explain explain, Pageable pageable) {
        return explainRepository.findAll(select(explain),pageable);
    }

    @Override
    public List<Explain> FindMyExplain(BoaUser boaUser,Explain explain) {
        explain.setStatus(1);//已发布
        List<Explain> explains = findAll(explain);
        List<Explain> new_exp=new ArrayList<>();
        for(Explain exp:explains){
            Integer cId = exp.getCId();
            int claId = boaUser.getGroupId();
            List<Map<String, Object>> teaClasses = classService.getTeaClasses(cId);
            if(teaClasses!=null&&teaClasses.size()>0){
                //任教班级的人才有权限查看讲解视频
                List<Object> classIds = teaClasses.stream().map(m -> m.get("classId")).collect(Collectors.toList());
                if(classIds.contains(claId))new_exp.add(exp);
            }
        }
        return new_exp;
    }

    @Override
    public List<Explain> findAll(Explain explain) {
        return explainRepository.findAll(select(explain));
    }

    @Override
    public void save(Explain explain) {
        explainRepository.save(explain);
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        explainRepository.deleteAll(ids);
    }

    @Override
    public Explain findById(Integer id) {
        return explainRepository.findById(id).get();
    }

    private Specification select(Explain explain) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (explain != null) {
                    if (explain.getCId() != null) {
                        predicates.add(cb.equal(root.get("cId"), explain.getCId()));
                    }
                    if (explain.getGradeId() != null) {
                        predicates.add(cb.equal(root.get("gradeId"), explain.getGradeId()));
                    }
                    if (explain.getSubjectId() != null) {
                        predicates.add(cb.equal(root.get("subjectId"), explain.getSubjectId()));
                    }
                    if (explain.getChapterId() != null) {
                        predicates.add(cb.equal(root.get("chapterId"), explain.getChapterId()));
                    }
                    if(explain.getStatus()!=null){
                        predicates.add(cb.equal(root.get("status"), explain.getStatus()));
                    }
                }
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

            }
        };
        return specification;
    }
}
