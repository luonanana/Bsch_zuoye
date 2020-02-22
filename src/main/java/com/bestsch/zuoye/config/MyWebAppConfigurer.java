package com.bestsch.zuoye.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @Value("${FILE_PATH}")
    private String FILE_PATH;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String ZUOYE_FILE_WEB_PATH = "/upload/zuoyeFile/";//作业题目文件
        String EXPLAIN_FILE_WEB_PATH = "/upload/explainFile/";//讲解视频文件
        String ANSWER_FILE_WEB_PATH = "/upload/answerFile/";//作业答案文件
        String REVIEW_FILE_WEB_PATH = "/upload/reviewFile/";//作业批阅文件
        registry.addResourceHandler(ANSWER_FILE_WEB_PATH+"**").addResourceLocations("file:"+FILE_PATH+"/answerFile/");
        registry.addResourceHandler(EXPLAIN_FILE_WEB_PATH+"**").addResourceLocations("file:"+FILE_PATH+"/explainFile/");
        registry.addResourceHandler(REVIEW_FILE_WEB_PATH+"**").addResourceLocations("file:"+FILE_PATH+"/reviewFile/");
        registry.addResourceHandler(ZUOYE_FILE_WEB_PATH+"**").addResourceLocations("file:"+FILE_PATH+"/zuoyeFile/");

    }
}
