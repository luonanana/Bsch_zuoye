package com.bestsch.zuoye.service.impl;

import com.bestsch.utils.DException;
import com.bestsch.zuoye.dao.UserZuoyeRepository;
import com.bestsch.zuoye.dao.ZuoyeObjectRepository;
import com.bestsch.zuoye.dao.ZuoyeQestionRepository;
import com.bestsch.zuoye.dao.ZuoyeRepository;
import com.bestsch.zuoye.enums.ZuoyeType;
import com.bestsch.zuoye.mem.GroupType;
import com.bestsch.zuoye.model.UserZuoye;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeObject;
import com.bestsch.zuoye.model.ZuoyeQestion;
import com.bestsch.zuoye.service.IZuoyeService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ZuoyeService implements IZuoyeService {
    @Autowired
    private ZuoyeRepository zuoyeRepository;
    @Autowired
    private UserZuoyeRepository userZuoyeRepository;
    @Autowired
    private ZuoyeObjectRepository zuoyeObjectRepository;
    @Autowired
    private ZuoyeQestionRepository zuoyeQestionRepository;

    @Override
    public ZuoYe save(ZuoYe zuoye, List<Integer> classIds, List<Integer> stuIds) {
        //1.先存作业
        zuoye = zuoyeRepository.save(zuoye);
        Integer zuoyeId = zuoye.getId();
        if (zuoyeId != null) {
            //2.如果是修改 则删除作业题目
            List<ZuoyeQestion> zuoyeQestions = zuoyeQestionRepository.findByZuoyeId(zuoyeId);
            if (zuoyeQestions != null & zuoyeQestions.size() > 0)
                zuoyeQestionRepository.deleteAll(zuoyeQestions);
        }
        List<ZuoyeQestion> qestionList = zuoye.getZuoyeQestionList();
        if (qestionList != null && qestionList.size() > 0) {
            Integer id = zuoye.getId();
            for (ZuoyeQestion zuoyeQestion : qestionList) {
                zuoyeQestion.setZuoyeId(id);
            }
            //2.存作业题目
            zuoyeQestionRepository.saveAll(qestionList);
        }

        List<ZuoyeObject> zyObjList = new ArrayList<>();
        if (classIds != null && classIds.size() != 0) {//区分教师发布班级还是某人
            for (Integer groupId : classIds) {
                ZuoyeObject zuoyeObject = new ZuoyeObject();
                zuoyeObject.setClaId(groupId);
                zuoyeObject.setZuoyeId(zuoyeId);
                zyObjList.add(zuoyeObject);
            }
        } else {
            if (stuIds != null && stuIds.size() != 0) {
                for (Integer groupId : stuIds) {
                    ZuoyeObject zuoyeObject = new ZuoyeObject();
                    zuoyeObject.setStuId(groupId);
                    zuoyeObject.setZuoyeId(zuoyeId);
                    zyObjList.add(zuoyeObject);
                }
            }
        }
        List<ZuoyeObject> objects = zuoyeObjectRepository.findByZuoyeId(zuoyeId);
        if (objects != null && objects.size() > 0) zuoyeObjectRepository.deleteAll(objects);
        //3.存作业对象
        zuoyeObjectRepository.saveAll(zyObjList);
        return zuoye;
    }


    @Override
    public ZuoYe findById(Integer id) {
        return zuoyeRepository.findById(id).get();
    }

    @Override
    public void delete(Integer id) {
        List<UserZuoye> userZuoyes = userZuoyeRepository.findByZuoyeId(id);
        if (userZuoyes != null && userZuoyes.size() > 0) {
            throw new DException("作业和人有关联,不能删除");
        } else {
            List<ZuoyeObject> zuoyeObject = zuoyeObjectRepository.findByZuoyeId(id);
            //1.删除作业开放对象
            if (zuoyeObject != null && zuoyeObject.size() > 0)
                zuoyeObjectRepository.deleteAll(zuoyeObject);
            // List<ZuoyeQestion> zuoyeQestions = zuoyeQestionRepository.findByZuoyeId(id);
            //2.删除作业题目
            //if (zuoyeQestions != null & zuoyeQestions.size() > 0)
            // zuoyeQestionRepository.deleteAll(zuoyeQestions);
            //3.删除作业
            zuoyeRepository.deleteById(id);
        }

    }

    @Override
    public Page<ZuoYe> findAllByPage(ZuoYe zuoye, Pageable pageable) {
        return zuoyeRepository.findAll(select(zuoye,null), pageable);
    }

    @Override
    public List<ZuoYe> findAll(ZuoYe zuoye, List<Integer> zyIds) {
        return zuoyeRepository.findAll(select(zuoye,zyIds));
    }

    @Override
    public List<ZuoYe> findByZuoyeModelId(Integer id) {
        return zuoyeRepository.findByZuoyeModelId(id);
    }

    private Specification select(ZuoYe zuoye, List<Integer> zyIds) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (zuoye != null) {
                    if (zuoye.getCId() != null) {
                        predicates.add(cb.equal(root.get("cId"), zuoye.getCId()));
                    }
                    if (zuoye.getGradeId() != null) {
                        predicates.add(cb.equal(root.get("gradeId"), zuoye.getGradeId()));
                    }
                    if (zuoye.getSubjectId() != null) {
                        predicates.add(cb.equal(root.get("subjectId"), zuoye.getSubjectId()));
                    }
                    if (zuoye.getChapterId() != null) {
                        predicates.add(cb.equal(root.get("chapterId"), zuoye.getChapterId()));
                    }
                    if(zuoye.getStartDate()!=null){
                        predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), zuoye.getStartDate()));
                    }
                    if (zuoye.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), zuoye.getEndDate()));
                    }
                    if (StringUtils.isNotEmpty(zuoye.getName())) {

                        Predicate p1 = cb.like(root.get("name"), "%" + zuoye.getName() + "%");

                        Predicate p2 = cb.like(root.get("cName"), "%" + zuoye.getCName() + "%");

                        predicates.add(cb.or(p1, p2));
                    }
                }


                if (zyIds != null && zyIds.size() > 0) {
                    CriteriaBuilder.In<Object> in = cb.in(root.get("id"));
                    for (Integer id : zyIds) {
                        in.value(id);
                    }
                    predicates.add(in);
                }
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

            }
        };
        return specification;
    }

    public List<ZuoYe> findByType(Integer type) {
        ZuoYe zuoye = new ZuoYe();

        return zuoyeRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (zuoye != null) {


                }
                //查询作业截止日期大于今天
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                predicates.add(cb.greaterThanOrEqualTo(root.get("endDate").as(Date.class), calendar.getTime()));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

            }
        });
    }
}
