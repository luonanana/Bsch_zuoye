package com.bestsch.zuoye.controller.api;


import com.bestsch.openapi.BoaUser;
import com.bestsch.utils.DException;
import com.bestsch.utils.ErrorCode;
import com.bestsch.zuoye.config.Config;
import com.bestsch.zuoye.controller.BaseController;
import com.bestsch.zuoye.mem.UserAnswerMem2;
import com.bestsch.zuoye.model.UserAnswer;
import com.bestsch.zuoye.model.UserZuoye;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeQestion;
import com.bestsch.zuoye.service.IUserAnswerService;
import com.bestsch.zuoye.service.IUserZuoyeService;
import com.bestsch.zuoye.service.impl.ClassService;
import com.bestsch.zuoye.utils.FileUtil;
import net.sf.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(Config.API_PATH)
public class UserAnswerController extends BaseController {
    @Value("${FILE_PATH}")
    private String FILE_PATH;
    @Autowired
    private IUserAnswerService userAnswerService;
    @Autowired
    private IUserZuoyeService userZuoyeService;
    @Autowired
    private UserAnswerMem2 userAnswerMem2;
    @Autowired
    private ClassService classService;

    //答案统计
    @GetMapping("answerStatist")
    public Object tongji(@RequestParam("questionnaireId") String questionnaireId) {
        if (org.apache.commons.lang.StringUtils.isEmpty(questionnaireId) || !org.apache.commons.lang.StringUtils.isNumeric(questionnaireId))
            throw new DException(String.valueOf(ErrorCode.WRONG_REQUEST_PARAMS));
        Integer qId = Integer.parseInt(questionnaireId);

        Object obj = userAnswerService.AnswerStatistics(qId);

        if (obj != null) {
            return obj;
        } else
            return new ArrayList<>();

    }

    @PostMapping(value = "saveUserAnswer", produces = "application/json")
    public void saveUserAnswer(BoaUser authUser, @RequestBody ZuoYe zuoYe) {
        UserZuoye userZuoye=new UserZuoye();
        userZuoye.setZuoyeId(zuoYe.getId());
        userZuoye.setUserId(authUser.getId());
        userZuoye.setUserName(authUser.getName());
        userZuoye.setStatus(0);
        userZuoye.setDate(new Date());
        userZuoye.setClassId(authUser.getGroupId());
        Map<String,Object> cla =  classService.getClassInfo(authUser.getGroupId());
        userZuoye.setClassName(cla.get("className").toString());

        UserZuoye uQuestaire = userZuoyeService.save(userZuoye);

        List<UserAnswer> userAnswerList = new ArrayList<>();
        List<ZuoyeQestion> zuoyeQestion = zuoYe.getZuoyeQestionList();
        if (zuoyeQestion != null && zuoyeQestion.size() > 0) {
            for (ZuoyeQestion qs : zuoyeQestion) {
                UserAnswer userAnswer = new UserAnswer();
                userAnswer.setUserZuoyeId(uQuestaire.getId());
                userAnswer.setAnswer(qs.getAnswer());
                userAnswer.setZuoyeQestId(qs.getId());
                userAnswerList.add(userAnswer);
            }
        }
        userAnswerService.saveAll(userAnswerList);
    }

    //上传作业--只针对日常任务
    @PostMapping("UploadZuoye")
    public void UploadZuoye(UserAnswer userAnswer,@RequestParam(value = "file") MultipartFile[] files){
        /*Integer id = userAnswer.getId(); 上传作业是否可修改
        if(id!=null){
            UserAnswer answer  = userAnswerService.findById(id);
            userAnswer.setFilePyName(answer.getFilePyName());
            if(files.length>0){
                String[] split = answer.getFilePyName().split(",");
                for (String fielame : split) {
                    if (!StringUtils.isEmpty(fielame))
                        FileUtil.deleteFile(new File(FILE_PATH + "/reviewFile/" + fielame));
                }
            }else{
                userAnswer.setAnswer(answer.getAnswer());
            }
        }*/
        if (files.length == 0) throw new DException("请选择要上传的文件");
        if (files.length > 0) {
            StringBuffer sb=new StringBuffer();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = FileUtil.uploadImg(file, FILE_PATH + "reviewFile/");
                    sb.append(fileName+",");
                }
            }
            userAnswer.setAnswer(sb.toString());
        }
        userAnswerService.save(userAnswer);
    }



}
