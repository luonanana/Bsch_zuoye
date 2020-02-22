package com.bestsch.zuoye.utils;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * @author min
 * @description File工具类，扩展 hutool 工具包
 * @date 2019-07-05
 */
public class FileUtil  {

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * MultipartFile转File
     * @param multipartFile
     * @return
     */
    public static File toFile(MultipartFile multipartFile){
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix="."+getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File.createTempFile(IdUtil.simpleUUID(), prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除
     * @param files
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     * @param size
     * @return
     */
    public static String getSize(int size){
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }
    public static String uploadImg(byte[] imgData, String pathName) {
         String imgName = null;
        String path = null;
        File floorFile = null;
        try {
            //保存图片
            for (int i = 0; i < 100; i++) {
                imgName = UUID.randomUUID().toString().substring(0, 8)+".png";
                path = pathName + imgName;
                floorFile = new File(path);
                if (floorFile.exists()) continue;
                else break;
            }
            FileOutputStream fos = new FileOutputStream(floorFile);
            fos.write(imgData);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgName;
    }
    public static String uploadImg(MultipartFile file, String pathName) {
        String imgName = null;
        String path = null;
        File floorFile = null;
        try {
            //保存图片
            byte[] imgData = file.getBytes();
            String fileName = file.getOriginalFilename();
            int index = fileName.lastIndexOf(".");
            String ext = "";
            if (index > 0) {
                ext = fileName.substring(index);
            }
            for (int i = 0; i < 100; i++) {
                imgName = UUID.randomUUID().toString().substring(0, 8) + ext;
                path = pathName + imgName;
                System.out.println(path);
                floorFile = new File(path);
                // 检测是否存在目录
                if (!floorFile.getParentFile().exists()) {
                    floorFile.getParentFile().mkdirs();// 新建文件夹
                }
                if (floorFile.exists()) continue;
                else break;
            }
            FileOutputStream fos = new FileOutputStream(floorFile);
            fos.write(imgData);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgName;
    }
    /**
     * 下载文件
     *
     * @param response
     * @param filePath 需要下载附件的路径
     * @param fileName 下载附件的文件名	--- 显示给用户看的文件名（文件的原文件名）
     */
    public static void downloadFile(HttpServletResponse response, String filePath, String fileName) {
        OutputStream outp = null;
        try (FileInputStream in = new FileInputStream(filePath)){

            response.reset();
            response.setContentType("application/x-download;charset=UTF-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            //添加文件的头部
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            outp = response.getOutputStream();
            //循环出流中的数据
            byte[] b = new byte[1024];
            while (in.read(b) != -1) {
                outp.write(b);
            }
            outp.flush();
        } catch (Exception e) {

        } finally {
            if (outp != null) {
                try {
                    outp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
