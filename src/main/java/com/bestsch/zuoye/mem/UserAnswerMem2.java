package com.bestsch.zuoye.mem;

import com.bestsch.zuoye.dao.ZuoyeRepository;
import com.bestsch.zuoye.model.UserAnswer;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeQestion;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserAnswerMem2 {

    @Autowired
    private ZuoyeRepository zuoyeRepository;

    private Map<Integer, List<Map<String, Object>>> uAnswerStaData;
    private List<UserAnswer> uAnswerList;
    private List<ZuoYe> zuoyeList;


    synchronized
    public void saveExecutionList(List<UserAnswer> uAnswerList) {
       /* this.uAnswerList = uAnswerList;
        this.zuoyeList = zuoyeRepository.findAll();
        paseuAnswerData();*/
    }

    public void paseuAnswerData() {

        //解析数据进行统计
        //按照作业分

        if (uAnswerStaData == null) {
            uAnswerStaData = new HashMap<>();
        }
        Map<Integer, List<Map<String, Object>>> uAnswerStaData2 = new HashMap<>();
        if (uAnswerList != null && uAnswerList.size() > 0) {
                //根据作业分组
                try {
                    Map<Integer, List<UserAnswer>> listMap = uAnswerList.stream().collect(Collectors.groupingBy(x -> x.getUserZuoye().getZuoyeId()));
                    for (ZuoYe zuoye :zuoyeList) {
                        Integer qId = zuoye.getId();
                        List<UserAnswer> quesionnaireAnswers = listMap.get(qId);
                        if(quesionnaireAnswers==null||quesionnaireAnswers.size()==0){
                            continue;
                        }

                        //作业题目统计list
                        List<Map<String, Object>> quesionnaireSta = new ArrayList<>();
                        uAnswerStaData2.put(qId,quesionnaireSta);

                        List<ZuoyeQestion> zuoyeQestionList = zuoye.getZuoyeQestionList();
                        //根据作业题目分组
                        Map<Integer, List<UserAnswer>> collect = quesionnaireAnswers.stream().collect(Collectors.groupingBy(UserAnswer::getZuoyeQestId));
                        for (ZuoyeQestion zuoyeQestion : zuoyeQestionList) {

                            Integer qestId = zuoyeQestion.getId();
                            Integer typeId = zuoyeQestion.getQuestionTypeId();
                            List<UserAnswer> questionAnswers = collect.get(qestId);

                            //作业题目统计Map
                            Map<String,Object> questionSta = new HashMap<>();
                            quesionnaireSta.add(questionSta);
                            questionSta.put("id",qestId);
                            questionSta.put("name",zuoyeQestion.getName());
                            questionSta.put("type",zuoyeQestion.getQuestionTypeId());
                            questionSta.put("testCount",0);

                            if(typeId==4||typeId==5||typeId==6){
                                continue;
                            }

                            String config = zuoyeQestion.getConfig();

                            JSONObject json = JSONObject.fromObject(config);
                            String options = json.optString("options");
                            String rangeNumStr = json.optString("rangeNum");
                            String questions = json.optString("questions");

                            JSONArray arr_options = new JSONArray();
                            if (StringUtils.isNotEmpty(options)) {
                                arr_options = JSONArray.fromObject(options);
                            }

                            JSONArray arr_questions = null;
                            if (StringUtils.isNotEmpty(questions)) {

                                arr_questions = JSONArray.fromObject(questions);
                                arr_questions = JSONArray.fromObject(questions);
                            }

                            int answersCount = 1;
                            //对每个做题记录进行统计
                            for (UserAnswer u :questionAnswers) {
                                String answer = u.getAnswer();
                                JSONArray arr_answers = new JSONArray();

                                if (StringUtils.isNotEmpty(answer)) {
                                    arr_answers = JSONArray.fromObject(answer);
                                }

                                if(arr_answers.size()!=0){
                                    int testCount = (int) questionSta.get("testCount");
                                    questionSta.put("testCount",testCount+1);
                                }

                                //单选，多选，下拉
                                if(typeId==1||typeId==2||typeId==3){

                                    List<Map<String,Object>> optionData = (List<Map<String, Object>>) questionSta.get("optionData");
                                    if(optionData==null){
                                        optionData = new ArrayList<>();
                                        questionSta.put("optionData",optionData);

                                        for (int j = 0; j < arr_options.size() ; j++) {
                                            Map<String,Object> optionItem = new HashMap<>();
                                            optionItem.put("index",j);
                                            optionItem.put("name",arr_options.get(j));
                                            optionItem.put("selectCount",0);
                                            optionData.add(optionItem);
                                        }
                                    }

                                    for (int j = 0; j < arr_answers.size(); j++) {
                                        Integer selectAnswer = Integer.parseInt(String.valueOf(arr_answers.get(j)));
                                        for (int k = 0; k < optionData.size(); k++) {
                                            Map<String,Object> optionItem = optionData.get(k);

                                            if(selectAnswer== Integer.parseInt(String.valueOf(optionItem.get("index")))){
                                                Integer selectCount = Integer.parseInt(String.valueOf(optionItem.get("selectCount")));
                                                optionItem.put("selectCount",selectCount+1);
                                            }
                                        }
                                    }



                                }
                                else if(typeId==7){
                                    List<Map<String,Object>> quesionData = (List<Map<String, Object>>) questionSta.get("quesionData");
                                    if(quesionData==null){
                                        quesionData = new ArrayList<>();
                                        questionSta.put("quesionData",quesionData);

                                        for (int j = 0; j < arr_questions.size() ; j++) {
                                            Map<String,Object> quesionItem = new HashMap<>();

                                            List<Map<String,Object>> optionData = new ArrayList<>();
                                            quesionItem.put("index",j);
                                            quesionItem.put("name",arr_questions.get(j));
                                            quesionItem.put("optionData",optionData);
                                            quesionData.add(quesionItem);

                                            for (int k = 0; k < arr_options.size() ; k++) {
                                                Map<String,Object> optionItem = new HashMap<>();
                                                optionItem.put("index",k);
                                                optionItem.put("name",arr_options.get(k));
                                                optionItem.put("selectCount",0);
                                                optionData.add(optionItem);
                                            }
                                        }
                                    }

                                    for (int j = 0; j < arr_answers.size(); j++) {
                                        if(arr_answers.get(j).equals("null")){
                                            continue;
                                        }

                                        Integer selectAnswer = Integer.parseInt(String.valueOf(arr_answers.get(j)));

                                        List<Map<String,Object>> optionData = (List<Map<String, Object>>) quesionData.get(j).get("optionData");


                                        Map<String,Object> optionItem = optionData.get(j);

                                        if(selectAnswer== Integer.parseInt(String.valueOf(optionItem.get("index")))){
                                            Integer selectCount = Integer.parseInt(String.valueOf(optionItem.get("selectCount")));
                                            optionItem.put("selectCount",selectCount+1);
                                        }

                                    }

                                }
                                else if(typeId==8){
                                    List<Map<String,Object>> quesionData = (List<Map<String, Object>>) questionSta.get("quesionData");
                                    if(quesionData==null){
                                        quesionData = new ArrayList<>();
                                        questionSta.put("quesionData",quesionData);

                                        for (int j = 0; j < arr_questions.size() ; j++) {
                                            Map<String,Object> quesionItem = new HashMap<>();

                                            List<Map<String,Object>> optionData = new ArrayList<>();
                                            quesionItem.put("index",j);
                                            quesionItem.put("name",arr_questions.get(j));
                                            quesionItem.put("optionData",optionData);
                                            quesionData.add(quesionItem);

                                            for (int k = 0; k < arr_options.size() ; k++) {
                                                Map<String,Object> optionItem = new HashMap<>();
                                                optionItem.put("index",k);
                                                optionItem.put("name",arr_options.get(k));
                                                optionItem.put("selectCount",0);
                                                optionData.add(optionItem);
                                            }
                                        }
                                    }

                                    for (int j = 0; j < arr_answers.size(); j++) {
                                        JSONArray answerItem = JSONArray.fromObject(arr_answers.get(j));

                                        List<Map<String,Object>> optionData = (List<Map<String, Object>>) quesionData.get(j).get("optionData");

                                        for (int l = 0; l < answerItem.size(); l++) {

                                            Integer selectAnswer = Integer.parseInt(String.valueOf(answerItem.get(l)));

                                            for (int m = 0; m < optionData.size(); m++) {
                                                Map<String,Object> optionItem = optionData.get(m);

                                                if(selectAnswer== Integer.parseInt(String.valueOf(optionItem.get("index")))){
                                                    Integer selectCount = Integer.parseInt(String.valueOf(optionItem.get("selectCount")));
                                                    optionItem.put("selectCount",selectCount+1);
                                                }
                                            }

                                        }

                                    }

                                }
                                else if(typeId==9){
                                    Integer rangeNum = Integer.parseInt(rangeNumStr);
                                    List<Map<String,Object>> optionData = (List<Map<String, Object>>) questionSta.get("optionData");
                                    if(optionData==null){
                                        optionData = new ArrayList<>();
                                        questionSta.put("optionData",optionData);
                                        questionSta.put("avgScore",0.0);

                                        for (int j = 0; j < rangeNum ; j++) {
                                            Map<String,Object> optionItem = new HashMap<>();
                                            optionItem.put("index",j);
                                            optionItem.put("name",j+1);
                                            optionItem.put("selectCount",0);
                                            optionData.add(optionItem);
                                        }
                                    }

                                    for (int j = 0; j < arr_answers.size(); j++) {
                                        Integer selectAnswer = Integer.parseInt(String.valueOf(arr_answers.get(j)));

                                        double avgScore = (double) questionSta.get("avgScore");

                                        avgScore = (avgScore*answersCount+selectAnswer)/(answersCount);

                                        questionSta.put("avgScore",avgScore);


                                        for (int k = 0; k < optionData.size(); k++) {
                                            Map<String,Object> optionItem = optionData.get(k);

                                            if(selectAnswer== Integer.parseInt(String.valueOf(optionItem.get("name")))){
                                                Integer selectCount = Integer.parseInt(String.valueOf(optionItem.get("selectCount")));
                                                optionItem.put("selectCount",selectCount+1);
                                            }
                                        }
                                    }

                                }
                                else if(typeId==10){
                                    Integer rangeNum = Integer.parseInt(rangeNumStr);
                                    List<Map<String,Object>> quesionData = (List<Map<String, Object>>) questionSta.get("quesionData");
                                    if(quesionData==null){
                                        quesionData = new ArrayList<>();
                                        questionSta.put("quesionData",quesionData);
                                        questionSta.put("avgScore",0.0);

                                        for (int j = 0; j < arr_questions.size() ; j++) {
                                            Map<String,Object> quesionItem = new HashMap<>();

                                            List<Map<String,Object>> optionData = new ArrayList<>();
                                            quesionItem.put("index",j);
                                            quesionItem.put("name",arr_questions.get(j));
                                            quesionItem.put("optionData",optionData);
                                            quesionItem.put("avgScore",0.0);

                                            quesionData.add(quesionItem);

                                            for (int k = 0; k < rangeNum; k++) {
                                                Map<String,Object> optionItem = new HashMap<>();
                                                optionItem.put("index",k);
                                                optionItem.put("name",k+1);
                                                optionItem.put("selectCount",0);
                                                optionData.add(optionItem);
                                            }
                                        }
                                    }

                                    double avgScore = 0.0;
                                    for (int j = 0; j < arr_answers.size(); j++) {
                                        JSONArray answerItem = JSONArray.fromObject(arr_answers.get(j));

                                        Map<String,Object> quesionItem = (Map<String, Object>)quesionData.get(j);
                                        List<Map<String,Object>> optionData = (List<Map<String, Object>>) quesionItem.get("optionData");


                                        double avgScore2 = (double) quesionItem.get("avgScore");

                                        for (int l = 0; l < answerItem.size(); l++) {

                                            Integer selectAnswer = Integer.parseInt(String.valueOf(answerItem.get(l)));
                                            avgScore2 = (avgScore2*answersCount+selectAnswer)/(answersCount);

                                            quesionItem.put("avgScore",avgScore2);

                                            for (int m = 0; m < rangeNum; m++) {
                                                Map<String,Object> optionItem = optionData.get(m);

                                                if(selectAnswer== Integer.parseInt(String.valueOf(optionItem.get("name")))){
                                                    Integer selectCount = Integer.parseInt(String.valueOf(optionItem.get("selectCount")));
                                                    optionItem.put("selectCount",selectCount+1);
                                                }
                                            }

                                        }

                                        avgScore+= avgScore2;

                                    }

                                    questionSta.put("avgScore",avgScore/quesionData.size());

                                }
                                else if(typeId==11){
                                    List<Map<String,Object>> optionData = (List<Map<String, Object>>) questionSta.get("optionData");
                                    if(optionData==null){
                                        optionData = new ArrayList<>();
                                        questionSta.put("optionData",optionData);

                                        for (int j = 0; j < arr_options.size() ; j++) {
                                            Map<String,Object> optionItem = new HashMap<>();
                                            optionItem.put("index",j);
                                            optionItem.put("name",arr_options.get(j));
                                            optionItem.put("selectCount",new int[arr_options.size()]);
                                            optionItem.put("avgScore",0.0);
                                            optionData.add(optionItem);
                                        }
                                    }

                                    for (int j = 0; j < arr_answers.size(); j++) {
                                        Integer selectAnswer = Integer.parseInt(String.valueOf(arr_answers.get(j)));
                                        for (int k = 0; k < optionData.size(); k++) {

                                            Map<String,Object> optionItem = optionData.get(k);
                                            if(selectAnswer== Integer.parseInt(String.valueOf(optionItem.get("index")))){
                                                int[] selectCount = (int[]) optionItem.get("selectCount");
                                                selectCount[j]++;

                                                int score = arr_answers.size()-j;
                                                double avgScore = (double) optionItem.get("avgScore");

                                                avgScore = (avgScore*answersCount+score)/(answersCount);

                                                optionItem.put("avgScore",avgScore);
                                            }


                                        }
                                    }

                                }
                                answersCount++;
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        System.out.println("统计结束。。。。2");
        uAnswerStaData = uAnswerStaData2;
    }


    public Object getAll() {

        return uAnswerStaData;
    }


    public Object getByQuesionnaireId(Integer zuoyeId) {
        return uAnswerStaData.get(zuoyeId);
    }

}
