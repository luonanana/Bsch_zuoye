package com.bestsch.zuoye.service.impl;

import com.bestsch.openapi.BoaGroup;
import com.bestsch.openapi.BoaUser;
import com.bestsch.zuoye.dao.UserAnswerRepository;
import com.bestsch.zuoye.dao.UserZuoyeRepository;
import com.bestsch.zuoye.dao.ZuoyeObjectRepository;
import com.bestsch.zuoye.dao.ZuoyeRepository;
import com.bestsch.zuoye.enums.ZuoyeStatus;
import com.bestsch.zuoye.model.UserAnswer;
import com.bestsch.zuoye.model.UserZuoye;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeObject;
import com.bestsch.zuoye.service.IUserZuoyeService;
import com.bestsch.zuoye.utils.PageUtil;
import com.bestsch.zuoye.utils.TimeUtils;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserZuoyeService implements IUserZuoyeService {
    @Autowired
    private UserZuoyeRepository userZuoyeRepository;
    @Autowired
    private ZuoyeObjectRepository zuoyeObjectRepository;
    @Autowired
    private ZuoyeRepository zuoyeRepository;
    @Autowired
    private ClassService classService;
    @Autowired
    private ZuoyeObjectService zuoyeObjectService;
    @Autowired
    private ZuoyeService zuoyeService;
    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Override
    public Page<UserZuoye> findAll(UserZuoye userZuoye, Pageable pageable) {
        return userZuoyeRepository.findAll(this.select(userZuoye), pageable);
    }

    @Override
    public List<UserZuoye> findAll(UserZuoye userZuoye) {
        return userZuoyeRepository.findAll(this.select(userZuoye));
    }

    @Override
    public List<Map<String, Object>> FindZuoyeReview(UserZuoye userZuoye, String page,String count) {
        List<Map<String, Object>> result=new ArrayList<>();
        List<UserZuoye> userZuoyes = userZuoyeRepository.findAll(this.select(userZuoye));
        Map<Integer, List<UserZuoye>> stat_userZuoyes = userZuoyes.stream().collect(Collectors.groupingBy(UserZuoye::getStatus));
        for (Map.Entry<Integer, List<UserZuoye>> stat_uz : stat_userZuoyes.entrySet()) {
            Map<String,Object> mapData=new HashMap();
            List<Map<String, Object>> data = new ArrayList<>();
            Integer status = stat_uz.getKey();
            Map<Integer, List<UserZuoye>> maps = stat_uz.getValue().stream().collect(Collectors.groupingBy(UserZuoye::getZuoyeId));
            for (Map.Entry<Integer, List<UserZuoye>> uz : maps.entrySet()) {
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> zyObj = new ArrayList<>();
                Integer zuoyeId = uz.getKey();
                if(status==1){//已批阅必须是所有作业都批阅才返回
                    List<UserZuoye> zuoyes = userZuoyeRepository.findByZuoyeIdAndStatus(zuoyeId, 0);
                    if(zuoyes!=null&&zuoyes.size()>0)continue;
                }
                ZuoYe zuoYe = zuoyeRepository.findById(zuoyeId).get();
                map.put("zuoYe", zuoYe);
                int total = 0;//作业推送对象
                List<ZuoyeObject> objects = zuoyeObjectRepository.findByZuoyeId(zuoyeId);
                if (objects != null && objects.size() > 0) {
                    List<Integer> claIds = objects.stream().filter(zuoyeObject -> zuoyeObject.getClaId() != null).map(ZuoyeObject::getClaId).collect(Collectors.toList());
                    List<Integer> studs = objects.stream().map(ZuoyeObject::getStuId).collect(Collectors.toList());
                    if (claIds != null && claIds.size() > 0) {//推送对象为班级
                        zyObj = classService.getClassInfo(claIds);
                        for (Integer claId : claIds) {
                            List<Map<String, Object>> stuInfos = (List<Map<String, Object>>) classService.getClassStuInfos(claId);
                            total = total + stuInfos.size();
                        }
                    } else {//推送对象为学生
                        total = studs.size();
                        zyObj = classService.getStuInfo(studs);
                    }
                }
                map.put("zyObj", zyObj);
                int userZuoySize = uz.getValue().size();
                int size = 0;

                if (status.equals(ZuoyeStatus.UNREVIEW.getId())) {//作业未批
                    size = userZuoyeRepository.findByZuoyeIdAndStatus(zuoyeId, ZuoyeStatus.REVIEW.getId()).size();
                    map.put("unReview", userZuoySize);
                    map.put("Review", size);
                } else {//已批
                    size = userZuoyeRepository.findByZuoyeIdAndStatus(zuoyeId, ZuoyeStatus.UNREVIEW.getId()).size();
                    map.put("Review", userZuoySize);
                    map.put("unReview", size);
                }
                int noComit = total - (userZuoySize + size);
                map.put("noComit", noComit);
                data.add(map);
            }
            PageUtil.pageBySubList(data, page, count);
            mapData.put("total", data.size());
            mapData.put("status",status);
            mapData.put("zuoye",data);
            result.add(mapData);
        }
        return result;
    }

    @Override
    public Map<String, Object> FindMyZuoye(BoaUser authUser,UserZuoye userZuoye, ZuoYe zuoYe, String page,String count) {
        Map<String, Object> map=new HashMap<>();
        List<Integer> roles = authUser.getRoleIds();// 1-教师， 3-学生， 4-家长
       if(!roles.contains(3)) return null;
        int classId = authUser.getGroupId();
        int userId = authUser.getId();
        ZuoyeObject obj_cla=new ZuoyeObject();//作业对象为班级
        ZuoyeObject obj_stu=new ZuoyeObject();//作业对象单独指派学生
        obj_cla.setClaId(classId);
        obj_stu.setStuId(userId);
        List<ZuoyeObject> zuoyeObjectList_cla = zuoyeObjectService.findAll(obj_cla);
        List<ZuoyeObject> zuoyeObjectList_stu = zuoyeObjectService.findAll(obj_stu);
        List<Integer> zuoyeIds=new ArrayList<>();
        if(zuoyeObjectList_cla!=null&&zuoyeObjectList_cla.size()>0)zuoyeIds=zuoyeObjectList_cla.stream().map(ZuoyeObject::getZuoyeId).distinct().collect(Collectors.toList());
        if(zuoyeObjectList_stu!=null&&zuoyeObjectList_stu.size()>0)zuoyeIds.addAll(zuoyeObjectList_stu.stream().map(ZuoyeObject::getZuoyeId).distinct().collect(Collectors.toList()));
        //全部作业
        List<ZuoYe> zuoYes = zuoyeService.findAll(zuoYe,zuoyeIds);
        //已完成的作业，若已批阅，则显示分数
        List<UserZuoye> complete_uz = findAll(userZuoye);
        List<Map<String,Object>> complete_zuoye=new ArrayList<>();
        for(UserZuoye uz:complete_uz){
            Map<String, Object> uz_map=new HashMap<>();
            Integer status = uz.getStatus();
            if(status==1){
                List<UserAnswer> answers = userAnswerRepository.findByUserZuoyeId(uz.getId());
                //分数
                Double fenshu = answers.stream().collect(Collectors.summingDouble(UserAnswer::getFenshu));
                uz_map.put("fenshu",fenshu);
                //总分
               // uz_map.put("totalScore",totalScore);
            }
            uz_map.put("zuoye",uz.getZuoye());
            complete_zuoye.add(uz_map);
        }

        List list = PageUtil.pageBySubList(complete_zuoye, page, count);
        //待完成的作业
        List<ZuoYe> noComplete_zuoye = zuoYes.stream().filter(item -> !complete_zuoye.contains(item)).collect(Collectors.toList());
        List list1 = PageUtil.pageBySubList(noComplete_zuoye, page, count);
        map.put("CompZuoYe",list);
        map.put("NoCompZuoYe",list1);
        map.put("CompZuoYeTotal",complete_zuoye.size());
        map.put("NoCompZuoYeTotal",noComplete_zuoye.size());
        return map;
    }

    @Override
    public List<Map<String, Object>> FindClassZuoye(UserZuoye userZuoye) {
        List<Map<String, Object>> data = new ArrayList<>();
        Integer zuoyeId = userZuoye.getZuoyeId();
        List<UserZuoye> userZuoyes = findAll(userZuoye);
        //不同班级的作业批阅
        Map<Integer, List<UserZuoye>> listMap = userZuoyes.stream().collect(Collectors.groupingBy(UserZuoye::getClassId));
        List<Integer> claIds = userZuoyes.stream().map(UserZuoye::getClassId).distinct().collect(Collectors.toList());
        for (Map.Entry<Integer, List<UserZuoye>> uz : listMap.entrySet()) {
            Map<String, Object> claMap = new HashMap<>();
            Integer claId = uz.getKey();
            List<UserZuoye> zuoyes = uz.getValue();
            List<Integer> userIds = zuoyes.stream().map(UserZuoye::getUserId).collect(Collectors.toList());
            claMap.put("claId", claId);
            claMap.put("claName", zuoyes.get(0).getClassName());
            //是否与批阅分组
            Map<Integer, List<UserZuoye>> collect = zuoyes.stream().collect(Collectors.groupingBy(UserZuoye::getStatus));
            List<UserZuoye> unReview_uzy = collect.get(ZuoyeStatus.UNREVIEW.getId());//未批阅
            List<UserZuoye> review_uzy = collect.get(ZuoyeStatus.REVIEW.getId());//已批阅
            List<Map<String, Object>> noCommitStu=new ArrayList();
            //已批阅则有分数
            if (review_uzy != null && review_uzy.size() > 0) {
                for (UserZuoye uzy : review_uzy) {
                    List<UserAnswer> userAnswer = userAnswerRepository.findByUserZuoyeId(uzy.getId());
                    Double totalScore = userAnswer.stream().collect(Collectors.summingDouble(UserAnswer::getFenshu));
                    uzy.setTotalScore(totalScore);
                }
            }
            List<Map<String, Object>> stuInfos = (List<Map<String, Object>>) classService.getClassStuInfos(claId);
           for(Map<String, Object> stu:stuInfos){
               Integer userId = (Integer) stu.get("userId");
               if(!userIds.contains(userId)){
                   noCommitStu.add(stu);
               }
           }
            claMap.put("review", review_uzy);
            claMap.put("unReview", unReview_uzy);
            claMap.put("noCommit", noCommitStu);
            data.add(claMap);


        }
        List<ZuoyeObject> objects = zuoyeObjectRepository.findByZuoyeId(zuoyeId);
        if (objects != null && objects.size() > 0) {
            List<Integer> objIds = objects.stream().filter(zuoyeObject -> zuoyeObject.getClaId() != null).map(ZuoyeObject::getClaId).collect(Collectors.toList());
            if (objIds != null && objIds.size() > 0) {//推送对象为班级
                for (Integer id : objIds) {
                    if (!claIds.contains(id)) {
                        Map claMap = new HashMap();
                        claMap.put("claId", id);
                        List<Map<String, Object>> stuInfos = (List<Map<String, Object>>) classService.getClassStuInfos(id);
                        claMap.put("claName", stuInfos.size()==0?"":stuInfos.get(0).get("className"));
                        claMap.put("noCommit", stuInfos);

                    }
                }
            }
        }

            return data;
        }

        @Override
        public List<UserZuoye> findByZuoyeId (Integer qId){
            return userZuoyeRepository.findByZuoyeId(qId);
        }

        @Override
        public void deleteAll (List < UserZuoye > userZuoyes) {
            userZuoyeRepository.deleteAll(userZuoyes);
        }

        @Override
        public List<UserZuoye> findByUserId (Integer userId){
            return userZuoyeRepository.findByUserId(userId);
        }

        @Override
        public UserZuoye save (UserZuoye userZuoye){
            return userZuoyeRepository.save(userZuoye);
        }

        private void sort (List < Map < String, Object >> recyleData){
            Collections.sort(recyleData, ((o1, o2) -> {
                int i = 0;
                long dateTime1 = 0;
                long dateTime2 = 0;
                Date date1 = (Date) o1.get("dateTime");
                Date date2 = (Date) o2.get("dateTime");
                dateTime1 = date1.getTime();
                dateTime2 = date2.getTime();
                i = String.valueOf(dateTime1).compareTo(String.valueOf(dateTime2));
                return i;
            }));
        }

        private Specification select (UserZuoye userZuoye){
            Specification specification = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    if (userZuoye != null) {
                        if (userZuoye.getZuoyeId() != null) {
                            predicates.add(cb.equal(root.get("zuoyeId"), userZuoye.getZuoyeId()));
                        }
                        if (userZuoye.getUserId() != null) {
                            predicates.add(cb.equal(root.get("userId"), userZuoye.getUserId()));
                        }
                        if (userZuoye.getStatus() != null) {
                            predicates.add(cb.equal(root.get("status"), userZuoye.getStatus()));
                        }
                        if (userZuoye.getZuoye() != null) {
                            if(userZuoye.getZuoye().getCId()!=null){
                                predicates.add(cb.equal(root.get("zuoye").get("cId"), userZuoye.getZuoye().getCId()));
                            }
                            if (userZuoye.getZuoye().getStartDate() != null) {
                                predicates.add(cb.greaterThanOrEqualTo(root.get("zuoye").get("startDate"), userZuoye.getZuoye().getStartDate()));
                            }
                            if (userZuoye.getZuoye().getEndDate() != null) {
                                predicates.add(cb.lessThanOrEqualTo(root.get("zuoye").get("endDate"), userZuoye.getZuoye().getEndDate()));
                            }
                            if (StringUtils.isNotEmpty(userZuoye.getZuoye().getName())) {
                                Predicate p1 = cb.like(root.get("zuoye").get("name"), "%" + userZuoye.getZuoye().getName() + "%");
                                predicates.add(p1);
                            }

                        }
                    }
                    return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

                }
            };
            return specification;
        }
    }
