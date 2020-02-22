package com.bestsch.zuoye.controller.api;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.bestsch.openapi.BoaUser;
import com.bestsch.utils.DException;
import com.bestsch.utils.ErrorCode;
import com.bestsch.zuoye.config.Config;
import com.bestsch.zuoye.controller.BaseController;
import com.bestsch.zuoye.entity.Page;
import com.bestsch.zuoye.model.UserAnswer;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeObject;
import com.bestsch.zuoye.model.ZuoyeQestion;
import com.bestsch.zuoye.service.*;
import com.bestsch.zuoye.utils.EasyPoiUtils;
import com.bestsch.zuoye.utils.FileUtil;
import com.bestsch.zuoye.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(Config.API_PATH)
public class ZuoyeController extends BaseController {
    @Autowired
    private IZuoyeService zuoyeService;
    @Autowired
    private IZuoyeQestionService zuoyeQestionService;
    @Value("${bsch.open.base-host}")
    private String BSCH_BASE_HOST;
    @Value("${FILE_PATH}")
    private String FILE_PATH;
    @Value("${FILE_WEB_PATH}")
    private String FILE_WEB_PATH;


    @GetMapping("FindZuoye")
    public Object findAll(ZuoYe zuoye, @RequestParam("page") String page, @RequestParam("count") String count) {
        Page pageVO = PageUtil.getPageVO(page, count);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageVO.getCurrent() - 1, pageVO.getPageSize(), sort);

        return zuoyeService.findAllByPage(zuoye, pageable);
    }

    @GetMapping("GetZuoyeById")
    public Object GetZuoyeById(@RequestParam("id") String idStr) {
        if (StringUtils.isEmpty(idStr) || !StringUtils.isNumeric(idStr))
            throw new DException(String.valueOf(ErrorCode.WRONG_REQUEST_PARAMS));
        Integer id = Integer.parseInt(idStr);

        ZuoYe zuoye = zuoyeService.findById(id);
        List<ZuoyeQestion> zuoyeQestionList = zuoye.getZuoyeQestionList();

        if (zuoyeQestionList != null && zuoyeQestionList.size() != 0) {
            for (ZuoyeQestion zuoyeQestion : zuoyeQestionList) {
                String[] split = zuoyeQestion.getFileName().split(",");
                StringBuffer sb=new StringBuffer();
                for (String fielName : split) {
                    if (!StringUtils.isEmpty(fielName)) {
                        sb.append( FILE_WEB_PATH + "zuoyeFile/" + fielName+",");
                    }
                }
                zuoyeQestion.setFileName(sb.toString());
            }
        }
        return zuoye;
    }

    @PostMapping(value = "SaveZuoye")
    public ZuoYe SaveZuoye(BoaUser authUser,ZuoYe zuoye,@RequestParam(value = "classIds",required = false) List<Integer> classIds,  @RequestParam(value = "stuIds",required = false) List<Integer> stuIds,@RequestParam("file") MultipartFile[] files) {
        if (files.length == 0) throw new DException("请选择要上传的文件");
        Integer zuoyeId = zuoye.getId();
        if(zuoyeId!=null){
            List<ZuoyeQestion> zuoyeQestionList = zuoyeQestionService.findByZuoyeId(zuoyeId);
            if(zuoyeQestionList!=null&&zuoyeQestionList.size()>0){
                for(ZuoyeQestion zuoyeQestion:zuoyeQestionList) {
                    String[] split = zuoyeQestion.getFileName().split(",");
                    //查询源文件，并删除
                    for (String fielame : split) {
                        if (!StringUtils.isEmpty(fielame))
                            FileUtil.deleteFile(new File(FILE_PATH + "/zuoyeFile/" + fielame));
                    }
                }

            }
        }else{
            zuoye.setCId(authUser.getId());
            zuoye.setCName(authUser.getName());
            zuoye.setCDate(new Date());
        }
        if (files.length > 0) {
            StringBuffer sb=new StringBuffer();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = FileUtil.uploadImg(file, FILE_PATH + "zuoyeFile/");
                    sb.append(fileName+",");
                }
            }
            List<ZuoyeQestion> zuoyeQestions = new ArrayList<>();
            ZuoyeQestion zuoyeQestion = new ZuoyeQestion();
            zuoyeQestion.setFileName(sb.toString());
            zuoyeQestions.add(zuoyeQestion);
            zuoye.setZuoyeQestionList(zuoyeQestions);
        }


        return zuoyeService.save(zuoye,classIds,stuIds);
    }


    @PostMapping(value = "SaveZuoye2", produces = "application/json")
    public ZuoYe SaveZuoye2(BoaUser authUser, @RequestBody ZuoYe zuoye,@RequestParam(value = "classIds",required = false) List<Integer> classIds,  @RequestParam(value = "stuIds",required = false) List<Integer> stuIds) {
        zuoye.setCId(authUser.getId());
        zuoye.setCName(authUser.getName());
        zuoye.setCDate(new Date());
        return zuoyeService.save(zuoye,classIds,stuIds);
    }


    @PostMapping("DeleteZuoye")
    public void DeleteZuoye(@RequestParam("id") String idStr) {
        if (StringUtils.isEmpty(idStr) || !StringUtils.isNumeric(idStr))
            throw new DException(String.valueOf(ErrorCode.WRONG_REQUEST_PARAMS));
        Integer id = Integer.parseInt(idStr);
        zuoyeService.delete(id);
    }




}
