package com.bestsch.zuoye.controller.api;


import com.bestsch.openapi.BoaModule;
import com.bestsch.openapi.BoaUser;
import com.bestsch.openapi.BschBaseOpenApi;
import com.bestsch.utils.DException;
import com.bestsch.utils.ErrorCode;
import com.bestsch.zuoye.config.Config;
import com.bestsch.zuoye.controller.BaseController;
import com.bestsch.zuoye.entity.FileMessage;
import com.bestsch.zuoye.entity.Page;
import com.bestsch.zuoye.model.Browse;
import com.bestsch.zuoye.model.Download;
import com.bestsch.zuoye.model.Explain;
import com.bestsch.zuoye.service.IBrowseService;
import com.bestsch.zuoye.service.IDownloadService;
import com.bestsch.zuoye.service.IExplainService;
import com.bestsch.zuoye.service.impl.ClassService;
import com.bestsch.zuoye.utils.FileUtil;
import com.bestsch.zuoye.utils.PageUtil;
import com.bestsch.zuoye.utils.VideoTransCodeUtil;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Config.API_PATH)
public class ExplainController extends BaseController {
    @Autowired
    private IExplainService explainService;
    @Autowired
    private IDownloadService downloadService;
    @Autowired
    private IBrowseService browseService;
    @Value("${FILE_PATH}")
    private String FILE_PATH;
    @Value("${FILE_WEB_PATH}")
    private String FILE_WEB_PATH;


    @GetMapping("FindExplain")
    public Object FindExplain(Explain explain, @RequestParam("page") String page, @RequestParam("count") String count) {
        Page pageVO = PageUtil.getPageVO(page, count);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageVO.getCurrent() - 1, pageVO.getPageSize(), sort);

        return explainService.findAll(explain,pageable);
    }
    @GetMapping("FindExpDetail")
    public Object FindExpDetail(BoaUser authUser,@RequestParam("id") String idStr){
        if(StringUtils.isEmpty(idStr))throw new DException(ErrorCode.WRONG_REQUEST_PARAMS);
        int explainId = Integer.parseInt(idStr);
        Explain explain=explainService.findById(explainId);
        explain.setVideoName(FILE_WEB_PATH+ "explainFile/"+explain.getVideoName());
        Integer chapterId = explain.getChapterId();
        Explain exp=new Explain();
        exp.setChapterId(chapterId);
        //相关讲解
        List<Explain> relExplain = explainService.findAll(exp);
        if(relExplain!=null&&relExplain.size()>0){
            for (int i = 0; i <relExplain.size() ; i++) {
                Integer id = relExplain.get(i).getId();
                if(explainId==id)relExplain.remove(i);
            }
        }
        //浏览量
        Browse browse=new Browse();
        browse.setExplainId(explainId);
        browse.setUId(authUser.getId());
        browse.setUName(authUser.getName());
        browse.setUDate(new Date());
        browseService .save(browse);

        Map map=new HashMap();
        map.put("explain",explain);
        map.put("relExplain",relExplain);
        return map;
    }
    @PostMapping(value = "SaveExplain", produces = "application/json")
    public void SaveExplain(BoaUser authUser, Explain explain, @RequestParam("file") MultipartFile file) {
        if (explain == null || file == null || file.isEmpty())
            throw new DException(ErrorCode.WRONG_REQUEST_PARAMS);

      //  String fileName = FileUtil.uploadImg(file, FILE_PATH + "explainFile/");
        FileMessage message = null;
        try {
            message = VideoTransCodeUtil.VideoTransCode(FILE_PATH + "explainFile/", file);
            String name = message.getFileName();
            Long videoTime = message.getVideoSize();

            explain.setVideoName(name + ".mp4");
            explain.setVideoPicture(name + ".jpg");
            explain.setVideoTime(videoTime);
            explain.setCId(authUser.getId());
            explain.setCName(authUser.getName());
            explain.setCDate(new Date());
            explainService.save(explain);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @GetMapping("downloadExplain")
    public void downloadExplain(BoaUser authUser,@RequestParam("id") String idStr, HttpServletResponse response){
        if(StringUtils.isEmpty(idStr))throw new DException(ErrorCode.WRONG_REQUEST_PARAMS);
        int explainId = Integer.parseInt(idStr);
        Explain explain=explainService.findById(explainId);
       String path= FILE_PATH + "explainFile/"+explain.getVideoName();
       FileUtil.downloadFile(response,path,explain.getName()+"."+FileUtil.getExtensionName(explain.getVideoName()));
        Download download=new Download();
        download.setExplainId(explainId);
        download.setUId(authUser.getId());
        download.setUName(authUser.getName());
        download.setUDate(new Date());
        downloadService.save(download);
    }

    @GetMapping("saveBrowse")
    public void saveBrowse(BoaUser authUser,@RequestParam("id") String idStr){
        if(StringUtils.isEmpty(idStr))throw new DException(ErrorCode.WRONG_REQUEST_PARAMS);
        int explainId = Integer.parseInt(idStr);
         Browse browse=new Browse();
        browse.setExplainId(explainId);
        browse.setUId(authUser.getId());
        browse.setUName(authUser.getName());
        browse.setUDate(new Date());
        browseService .save(browse);
    }

    //学生查看讲解列表
    @GetMapping("FindMyExplain")
    public Object FindMyExplain(BoaUser authUserWithDept,Explain explain, @RequestParam("page") String page, @RequestParam("count") String count) {
        List<Explain> explains = explainService.FindMyExplain(authUserWithDept, explain);
        PageUtil.pageBySubList(explains,page,count);
        Map map=new HashMap();
        map.put("explains",explains);
        map.put("total",explains.size());
        return map;
    }
}
