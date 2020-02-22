package com.bestsch.zuoye.utils;

import com.bestsch.zuoye.entity.Page;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class PageUtil {
    public static Page getPageVO(String page, String pageCount) {
        Page pageVO = new Page();
        if (StringUtils.isNumeric(pageCount)) {
            int num = Integer.valueOf(pageCount);
            /*if (num>50) {
                pageVO.setPageSize(50);
            }*/
            pageVO.setPageSize(Double.valueOf(pageCount).intValue());
        }
        if (StringUtils.isNumeric(page)) {
            pageVO.setCurrent(page);
        }
        return pageVO;
    }
    public static List pageBySubList(List list, String page, String pageCount) {
        Page pageVO = getPageVO(page, pageCount);
        Integer pageSize = pageVO.getPageSize();
        Integer pageNum = pageVO.getCurrent() == 0 ? 1 : pageVO.getCurrent();

        //分页
        Integer totalNum = list.size();
        if (totalNum == 0) return null;

        int totalPage=0;
        if (totalNum > 0) {
            totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        }
        if (pageNum > totalPage) {
            pageNum = totalPage;
        }
        int startPoint = (pageNum - 1) * pageSize;
        int endPoint = startPoint + pageSize;
        if (totalNum <= endPoint) {
            endPoint = totalNum;
        }
        list = list.subList(startPoint, endPoint);
        return list;
    }
    public static List pageBySubList2(List list, String page, String pageCount) {
        Page pageVO = getPageVO(page, pageCount);
        Integer pagesize = pageVO.getPageSize();
        Integer currentPage = pageVO.getCurrent() == 0 ? 1 : pageVO.getCurrent();
        int totalNum = list.size();
        if (totalNum == 0) return null;
        int pagecount = 0;
        List<String> subList;
        int m = totalNum % pagesize;
        if (m > 0) {
            pagecount = totalNum / pagesize + 1;
        } else {
            pagecount = totalNum / pagesize;
        }
        if (m == 0) {
            subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
        } else {
            if (currentPage == pagecount) {
                subList = list.subList((currentPage - 1) * pagesize, totalNum);
            } else {
                subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
            }
        }
        return subList;
    }
}
