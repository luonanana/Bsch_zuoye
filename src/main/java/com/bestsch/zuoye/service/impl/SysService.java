package com.bestsch.zuoye.service.impl;


import com.bestsch.utils.DHttp;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;

public class SysService {

    @Value("${bsch.open.schbase-host}")
    private String BSCH_SCHBASE_HOST;

   //从学习中心-工具-小测验获取题目
    public Object getQuestion(){

        String resultData = DHttp.get(
                BSCH_SCHBASE_HOST + "/oapi/OATea/GetTeaAllClass?UserTeaSerID=" );
        System.out.println(resultData);

        JSONArray jsonArray = JSONArray.fromObject(resultData);

        return jsonArray;
    }
}
