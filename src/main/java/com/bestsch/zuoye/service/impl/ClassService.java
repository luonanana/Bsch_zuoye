package com.bestsch.zuoye.service.impl;


import com.bestsch.openapi.BoaGroup;
import com.bestsch.openapi.BoaUser;
import com.bestsch.openapi.BschBaseOpenApi;
import com.bestsch.utils.DHttp;
import com.bestsch.zuoye.mem.GroupMem;
import com.bestsch.zuoye.mem.GroupType;
import com.bestsch.zuoye.model.ZuoyeObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClassService {
    @Value("${bsch.open.schbase-host}")
    private String BSCH_SCHBASE_HOST;

    //获取任教班级
    public List<Map<String, Object>> getTeaClasses(Integer userId) {

//      http://schd.bestsch.com/oapi/OATea/GetTeaAllClass?UserTeaSerID=144140
        String resultData = DHttp.get(
                BSCH_SCHBASE_HOST + "/oapi/OATea/GetTeaAllClass?UserTeaSerID=" + userId);
//        System.out.println(BSCH_SCHBASE_HOST + "/oapi/OATea/GetTeaAllClass?UserTeaSerID=" + userId);
        System.out.println(resultData);

        JSONArray jsonArray = JSONArray.fromObject(resultData);

        List<Map<String, Object>> classList = new ArrayList<>();
        if (jsonArray.size() != 0) {
            for (Object obj : jsonArray) {
                Map<String, Object> cla = new HashMap<>();
                JSONObject jsonObject = JSONObject.fromObject(obj);
                cla.put("classId", (Integer) jsonObject.get("UserClassID"));
                cla.put("className", ((String) jsonObject.get("GradeName")) + ((String) jsonObject.get("ClassName")));
                classList.add(cla);
            }
        }
        return classList;
    }

    /**
     * 获取多个学生信息接口获取多个学生信息
     *
     * @param stuIds
     * @return
     */
    public List<Map<String, Object>> getStuInfo(List<Integer> stuIds) {
//       http://schd.bestsch.com/oapi/OAStu/GetStuIdslist?stuids=144193,144186,144188
        String stuId = stuIds.toString();
        stuId = stuId.substring(1, stuId.length() - 1).replace(" ", "");

        String resultData = DHttp.get(
                BSCH_SCHBASE_HOST + "/oapi/OAStu/GetStuIdslist?stuids=" + stuId);
        JSONArray jsonArray = JSONArray.fromObject(resultData);
        List<Map<String, Object>> stuList = new ArrayList<>();
        if (jsonArray.size() != 0) {
            for (Object obj : jsonArray) {
                Map<String, Object> stu = new HashMap<>();
                JSONObject jsonObject = JSONObject.fromObject(obj);
                stu.put("userId", (Integer) jsonObject.get("UserStuSerID"));
                stu.put("userName", ((String) jsonObject.get("StuName")));
                stuList.add(stu);
            }
        }
        return stuList;
    }

    /**
     * 获取班级学生
     *
     * @param classId
     * @return
     */
    public Object getClassStuInfos(Integer classId) {
//        String resultData = DHttp.get("http://sch.schsoft.cn//api/api/dbClass/GetClassStu?classid=" + classId);
        String resultData = DHttp.get(
                BSCH_SCHBASE_HOST + "/oapi/DbClass/GetClassStu?ClassID=" + classId);
        JSONArray jsonArray = JSONArray.fromObject(resultData);
        List<Map<String, Object>> stuList = new ArrayList<>();
        if (jsonArray.size() != 0) {
            for (Object obj : jsonArray) {
                Map<String, Object> stu = new HashMap<>();
                JSONObject jsonObject = JSONObject.fromObject(obj);
                stu.put("userId", (Integer) jsonObject.get("UserID"));
                stu.put("userName", ((String) jsonObject.get("StuName")));
                stu.put("className", (String) jsonObject.get("GradeName") + jsonObject.get("ClassName"));
                stuList.add(stu);
            }
        }
        return stuList;
    }

    /**
     * 获取班级信息
     *
     * @param
     * @return
     */
    public List<Map<String, Object>> getClassInfo(List<Integer> classIds) {
        //http://schd.bestsch.com/oapi/VSchClass/getClassByIds?ClassIds=5,434
        String claId = classIds.toString();
        claId = claId.substring(1, claId.length() - 1).replace(" ", "");

        String resultData = DHttp.get(
                BSCH_SCHBASE_HOST + "/oapi/VSchClass/getClassByIds?ClassIds=" + claId);
        JSONArray jsonArray = JSONArray.fromObject(resultData);
        List<Map<String, Object>> classList = new ArrayList<>();
        if (jsonArray.size() != 0) {
            for (Object obj : jsonArray) {
                Map<String, Object> cla = new HashMap<>();
                JSONObject jsonObject = JSONObject.fromObject(obj);
                cla.put("classId", (Integer) jsonObject.get("UserClassID"));
                cla.put("className", jsonObject.get("GradeName") + ((String) jsonObject.get("ClassName")));
                classList.add(cla);
            }
        }
        return classList;

    }

    /**
     * 获取全部学科
     *
     * @return
     */
    public Object getAllSubject() {
        //http://schd.bestsch.com/oapi/SubInfo/getAllSubject
        String resultData = DHttp.get(
                BSCH_SCHBASE_HOST + "/oapi/SubInfo/getAllSubject");
        JSONArray jsonArray = JSONArray.fromObject(resultData);
        return jsonArray;

    }

    public Object getChapter(String subId, Integer schId, Integer gradeId, Integer term) {
        //  http://learn.bestsch.com/ziyuan/api/GetTree?grade=11&subject=sub02&school=769&schyear=2020&term=1
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuilder url = new StringBuilder()
                .append("http://learn.bestsch.com/ziyuan/api/GetTree");
        StringBuilder paramStr = new StringBuilder()
                .append("grade=").append(gradeId)
                .append("&").append("subject=").append(subId)
                .append("&").append("school=").append(schId)
              //  .append("&").append("schyear=").append(2020)
                .append("&").append("term=").append(term);

        String res = DHttp.post(url.toString(), paramStr.toString().getBytes());
        JSONObject jsonObject = JSONObject.fromObject(res);
        Object result = jsonObject.get("tree");
        if(!result.equals("null")) {
            Map<String, Object> tree = (Map<String, Object>) jsonObject.get("tree");
            JSONArray jsonArray = (JSONArray) tree.get("child");

            if (jsonArray != null && jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    Map map = new HashMap();
                    JSONObject o = (JSONObject) jsonArray.get(i);
                    String name = (String) o.get("name");
                    Integer id = (Integer) o.get("id");
                    map.put("name", name);
                    map.put("id", id);
                    list.add(map);
                }
            }
        }
        return list;
    }


    public Map<String, Object> getClassInfo(Integer claId) {
        //http://schd.bestsch.com/oapi/VSchClass/getClassByIds?ClassIds=5,434

        String resultData = DHttp.get(
                BSCH_SCHBASE_HOST + "/oapi/VSchClass/getClassByIds?ClassIds=" + claId);
        JSONArray jsonArray = JSONArray.fromObject(resultData);

        Map<String, Object> cla = new HashMap<>();
        if (jsonArray.size() != 0) {

            JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(0));
            cla.put("classId", (Integer) jsonObject.get("UserClassID"));
            cla.put("className", jsonObject.get("GradeName") + ((String) jsonObject.get("ClassName")));


        }
        return cla;

    }


}
