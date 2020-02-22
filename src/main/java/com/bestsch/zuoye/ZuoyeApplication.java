package com.bestsch.zuoye;

import com.bestsch.bschapistd.config.EnableBschApiStd;
import com.bestsch.bschsso.EnableBschSso;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
@EnableBschSso
@EnableBschApiStd
@ServletComponentScan
public class ZuoyeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuoyeApplication.class, args);
    }

}
