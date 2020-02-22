package com.bestsch.zuoye.controller.api;


import com.bestsch.openapi.BoaGroup;
import com.bestsch.openapi.BoaModule;
import com.bestsch.openapi.BoaUser;
import com.bestsch.openapi.BschBaseOpenApi;

import com.bestsch.utils.DException;
import com.bestsch.utils.ErrorCode;
import com.bestsch.zuoye.config.Config;
import com.bestsch.zuoye.entity.Grade;
import com.bestsch.zuoye.enums.GradeEnum;
import com.bestsch.zuoye.service.impl.ClassService;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping(Config.API_PATH)
public class SysController {

    @Autowired
    private BschBaseOpenApi bschBaseOpenApi;
    @Autowired
    private ClassService classService;


    @GetMapping("GetUserMod")
    public List<BoaModule> getUserMod(BoaUser authUserWithDept) {
        return bschBaseOpenApi.getUserModule(authUserWithDept.getId());
    }

    @GetMapping("GetUserInfo")
    public BoaUser getUserInfo(BoaUser authUserWithDept) {
        return authUserWithDept;
    }

    @GetMapping("GetTeaClasses")
    public Object getTeaClasses(BoaUser authUserWithDept) {
        List<Map<String, Object>> teaClasses = classService.getTeaClasses(authUserWithDept.getId());
        for (Map map : teaClasses) {
            Integer id = (Integer) map.get("classId");
            JSONArray jsonArray = JSONArray.fromObject(classService.getClassStuInfos(id));
            map.put("students", jsonArray);
        }
        return teaClasses;
    }

    @GetMapping("GetGradeList")
    public Object getGradeList() {
        List<GradeEnum> list = GradeEnum.list();
        List<Grade> gradeList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Grade grade = new Grade();
            grade.setSystem_garde(list.get(i).getSystem_garde());
            grade.setName(list.get(i).getName());
            gradeList.add(grade);
        }
        return gradeList;
    }
    @GetMapping("GetAllSubject")
    public  Object GetAllSubject(){
      return   classService.getAllSubject();
    }
    //获取知识章节
    @GetMapping("GetChapter")
    public  Object GetChapter(BoaUser authUserWithDept,@RequestParam("subjectId") String subjectId,@RequestParam("gradeId") String gradeId,@RequestParam("term") String termStr){
        Integer schId = null;
        if (StringUtils.isEmpty(subjectId)||StringUtils.isEmpty(gradeId) || StringUtils.isEmpty(termStr))
            throw new DException(String.valueOf(ErrorCode.WRONG_REQUEST_PARAMS));

        BoaGroup dept = authUserWithDept.getDept();
        if (dept != null) {
            int type = authUserWithDept.getDept().getType();
            if (type == 2) {// 2部门是学校，5部门是教育局
                schId = dept.getId();
            }
        }
        Integer graId=Integer.parseInt(gradeId);
        Integer term=Integer.parseInt(termStr);
        return   classService.getChapter(subjectId,schId,graId,term);
    }

}
