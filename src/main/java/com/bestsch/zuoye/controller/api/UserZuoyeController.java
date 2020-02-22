package com.bestsch.zuoye.controller.api;

import com.bestsch.openapi.BoaUser;
import com.bestsch.utils.DException;
import com.bestsch.utils.ErrorCode;
import com.bestsch.zuoye.config.Config;
import com.bestsch.zuoye.controller.BaseController;
import com.bestsch.zuoye.model.UserAnswer;
import com.bestsch.zuoye.model.UserZuoye;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeQestion;
import com.bestsch.zuoye.service.*;
import com.bestsch.zuoye.utils.FileUtil;
import com.bestsch.zuoye.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(Config.API_PATH)
public class UserZuoyeController extends BaseController {
    @Autowired
    private IZuoyeService zuoyeService;
    @Autowired
    private IUserZuoyeService userZuoyeService;
    @Autowired
    private IZuoyeQestionService zuoyeQestionService;
    @Autowired
    private IUserAnswerService userAnswerService;
    @Value("${FILE_PATH}")
    private String FILE_PATH;

    //作业批阅列表
    @GetMapping("FindZuoyeReview")
    public Object FindZuoyeReview(BoaUser authUser, ZuoYe zuoye, @RequestParam("page") String page, @RequestParam("count") String count) {
        Map<String, Object> map = new HashMap<>();
        UserZuoye userZuoye=new UserZuoye();
        zuoye.setCId(authUser.getId());
        userZuoye.setZuoye(zuoye);
        //我发布的
        List<ZuoYe> myCreate = zuoyeService.findAll(zuoye,null);
        //收到作业
        List<UserZuoye> myAbout = userZuoyeService.findAll(userZuoye);
        map.put("myCreate",myCreate.size());
        map.put("myAbout",myAbout.size());
        List<Map<String, Object>> mapList = userZuoyeService.FindZuoyeReview(userZuoye,page,count);
        map.put("content", mapList);


        return map;
    }
   //不同班级作业列表
    @GetMapping("FindClassZuoye")
    public Object FindClassZuoye(UserZuoye userZuoye, @RequestParam("page") String page, @RequestParam("count") String count) {
        return userZuoyeService.FindClassZuoye(userZuoye);
    }
    //查找学生作业
    @GetMapping("FindUserZuoye")
    public Object FindUserZuoye(UserZuoye userZuoye) {
        Map map=new HashMap();
        Integer zuoyeId = userZuoye.getZuoyeId();
        ZuoYe zuoYe = zuoyeService.findById(zuoyeId);
        map.put("zuoYe",zuoYe);
        List<UserZuoye> all = userZuoyeService.findAll(userZuoye);
        if(all!=null&&all.size()>0){
            UserZuoye uz = all.get(0);
            Integer uzId = uz.getId();
            UserAnswer userAnswer=new UserAnswer();
            userAnswer.setUserZuoyeId(uzId);
            List<UserAnswer> answers = userAnswerService.findAll(userAnswer);
            map.put("answers",answers);
        }else {
            //用户没有提交该作业
            List<ZuoyeQestion> qestions = zuoyeQestionService.findByZuoyeId(zuoyeId);
            map.put("qestions",qestions);
        }
        return map;
    }

    //主观题老师批阅
    @PostMapping("QuestReview")
    public void QuestReview(UserAnswer userAnswer,@RequestParam(value = "file",required = false) MultipartFile[] files){
        Integer id = userAnswer.getId();
        if(id!=null){
            UserAnswer answer  = userAnswerService.findById(id);
            userAnswer.setAnswer(answer.getAnswer());
            if(files.length>0){
                String[] split = answer.getFilePyName().split(",");
                for (String fielame : split) {
                    if (!StringUtils.isEmpty(fielame))
                        FileUtil.deleteFile(new File(FILE_PATH + "/reviewFile/" + fielame));
                }
            }else{
               userAnswer.setFilePyName(answer.getFilePyName());
           }
        }
        if (files.length > 0) {
            StringBuffer sb=new StringBuffer();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = FileUtil.uploadImg(file, FILE_PATH + "reviewFile/");
                    sb.append(fileName+",");
                }
            }
            userAnswer.setFilePyName(sb.toString());
            }

        userAnswerService.save(userAnswer);
    }

    //我的作业列表
    @GetMapping("FindMyZuoye")
    public Object FindMyZuoye(BoaUser authUserWithDept,ZuoYe zuoye, @RequestParam("page") String page, @RequestParam("count") String count){
        UserZuoye userZuoye=new UserZuoye();
        userZuoye.setUserId(authUserWithDept.getId());
        userZuoye.setZuoye(zuoye);
        Map<String, Object> map = userZuoyeService.FindMyZuoye(authUserWithDept, userZuoye,zuoye,page,count);
        return map;

    }
    //错题列表
    @GetMapping("FindWrongQuest")
    public Object FindWrongQuest(BoaUser authUserWithDept,ZuoYe zuoye, @RequestParam("page") String page, @RequestParam("count") String count){
        UserZuoye userZuoye=new UserZuoye();
        userZuoye.setUserId(authUserWithDept.getId());
        userZuoye.setZuoye(zuoye);
        Map<String, Object> map = userZuoyeService.FindMyZuoye(authUserWithDept, userZuoye,zuoye,page,count);
        return map;

    }
}
