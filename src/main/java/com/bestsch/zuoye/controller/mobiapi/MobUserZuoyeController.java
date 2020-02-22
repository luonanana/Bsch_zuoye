package com.bestsch.zuoye.controller.mobiapi;

import com.bestsch.openapi.BoaUser;
import com.bestsch.openapi.BschBaseOpenApi;
import com.bestsch.utils.DException;
import com.bestsch.utils.ErrorCode;
import com.bestsch.zuoye.config.Config;
import com.bestsch.zuoye.controller.BaseController;
import com.bestsch.zuoye.model.UserAnswer;
import com.bestsch.zuoye.model.UserZuoye;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeQestion;
import com.bestsch.zuoye.service.IUserAnswerService;
import com.bestsch.zuoye.service.IUserZuoyeService;
import com.bestsch.zuoye.service.IZuoyeQestionService;
import com.bestsch.zuoye.service.IZuoyeService;
import com.bestsch.zuoye.utils.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Config.MOBILEAPI_PATH)
public class MobUserZuoyeController extends BaseController {
    @Autowired
    private IZuoyeService zuoyeService;
    @Autowired
    private IUserZuoyeService userZuoyeService;
    @Autowired
    private IZuoyeQestionService zuoyeQestionService;
    @Autowired
    private IUserAnswerService userAnswerService;
    @Autowired
    private BschBaseOpenApi bschBaseOpenApi;
    @Value("${FILE_PATH}")
    private String FILE_PATH;

    //我的作业列表
    @GetMapping("FindMyZuoye")
    public Object FindMyZuoye(@RequestParam("userId") String userId, @RequestParam("page") String page, @RequestParam("count") String count, @RequestParam("timeIndex") String timeIndex) {
        Map<String, String> selectTime = new HashMap<>();
        if (!StringUtils.isEmpty(timeIndex)) {
            selectTime = selectTime(Integer.parseInt(timeIndex));
        }
        Integer uId = null;
        if (StringUtils.isEmpty(userId))
            throw new DException(String.valueOf(ErrorCode.WRONG_REQUEST_PARAMS));

        uId = Integer.parseInt(userId);
        BoaUser user =  bschBaseOpenApi.getUserInfoWithDept(uId);

      //  Map<String, Object> map = userZuoyeService.FindMyZuoye(user,selectTime,page, count);
        return null;

    }

    //查找学生作业
    @GetMapping("FindUserZuoye")
    public Object FindUserZuoye(UserZuoye userZuoye) {
        Map map = new HashMap();
        Integer zuoyeId = userZuoye.getZuoyeId();
        ZuoYe zuoYe = zuoyeService.findById(zuoyeId);
        map.put("zuoYe", zuoYe);
        List<UserZuoye> all = userZuoyeService.findAll(userZuoye);
        if (all != null && all.size() > 0) {
            UserZuoye uz = all.get(0);
            Integer uzId = uz.getId();
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setUserZuoyeId(uzId);
            List<UserAnswer> answers = userAnswerService.findAll(userAnswer);
            map.put("answers", answers);
        } else {
            //用户没有提交该作业
            List<ZuoyeQestion> qestions = zuoyeQestionService.findByZuoyeId(zuoyeId);
            map.put("qestions", qestions);
        }
        return map;
    }


    //错题列表
    @GetMapping("FindWrongQuest")
    public Object FindWrongQuest(BoaUser authUserWithDept, ZuoYe zuoye, @RequestParam("page") String page, @RequestParam("count") String count) {
        UserZuoye userZuoye = new UserZuoye();
        userZuoye.setUserId(authUserWithDept.getId());
        userZuoye.setZuoye(zuoye);
        Map<String, Object> map = userZuoyeService.FindMyZuoye(authUserWithDept, userZuoye, zuoye, page, count);
        return map;

    }
}
