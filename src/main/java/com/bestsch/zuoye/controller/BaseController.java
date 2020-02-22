package com.bestsch.zuoye.controller;

import com.bestsch.openapi.BoaUser;
import com.bestsch.zuoye.entity.Page;
import com.bestsch.zuoye.enums.TimeType;
import com.bestsch.zuoye.utils.CalendarUtils;
import com.bestsch.zuoye.utils.ChinaDate;
import com.bestsch.zuoye.utils.TimeUtils;
import org.apache.commons.lang.StringUtils;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author chengconghao
 * @description 进行信息校验什么的。
 * @date 2019-07-10
 */
public class BaseController {
    protected static Map<String, String> selectTime(Integer index) {
        try {
            Map<String, String> map = new HashMap<>();

            String startTime = "";
            String endTime = "";
            // indexStr 0代表什么，1代表什么...
            int currentYear = CalendarUtils.getYear();
            int currentMonth = CalendarUtils.getMonth();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String halfOfJan = ChinaDate.lunarToSolar(currentYear + "0115", false);
            Date halfJan = sdf.parse(halfOfJan);
            if (TimeType.CURRENT_TERM.getId() == index) {
                if (new Date().getTime() >= halfJan.getTime()) {
                    if (currentMonth < 9) {
                        startTime = halfOfJan + " 00:00:00";
                        endTime = currentYear + "-08-31 23:59:59";
                    } else {
                        startTime = currentYear + "-09-01 00:00:00";
                        endTime = ChinaDate.lunarToSolar((currentYear + 1) + "0115", false) + " 23:59:59";
                    }
                } else {
                    startTime = (currentYear - 1) + "-09-01 00:00:00";
                    endTime = halfOfJan + " 23:59:59";
                }
            } else if (TimeType.CURRENT_MONTH.getId() == index) {
                startTime = TimeUtils.getFirstDayOfCurrentMonth() + " 00:00:00";
                endTime = TimeUtils.getLastDayOfCurrentMonth() + " 23:59:59";
            } else if (TimeType.CURRENT_WEEK.getId() == index) {
                startTime = TimeUtils.getFirstOfWeek() + " 00:00:00";
                endTime = TimeUtils.getLastOfWeek() + " 23:59:59";
            } else if (TimeType.TODAY.getId() == index) {
                startTime = sdf.format(new Date()) + " 00:00:00";
                endTime = sdf.format(new Date()) + " 23:59:59";
            } else if (TimeType.TOTAL.getId() == index) {

            }
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
