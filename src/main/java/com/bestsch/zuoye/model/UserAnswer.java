package com.bestsch.zuoye.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户作业题目答案
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_answer")
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = UserAnswer.Update.class)
    private Integer id;

   /* @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;*/

    @Column(name = "answer")
    private String answer;//答案或者是图片提交的答案

    @Column(name = "file_py_name")
    private String filePyName;//答案是图片--老师批阅涂鸦

    @Column(name = "fenshu")
    private Double fenshu;//分数

    @Column(name = "zuoye_qest_id")
    private Integer zuoyeQestId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "zuoye_qest_id", insertable = false, updatable = false)
    private ZuoyeQestion zuoyeQestion;

    @Column(name = "user_zuoye_id")
    private Integer userZuoyeId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_zuoye_id", insertable = false, updatable = false)
    private UserZuoye userZuoye;


    public @interface Update {
    }


}
