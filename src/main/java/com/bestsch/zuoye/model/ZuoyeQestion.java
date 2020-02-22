package com.bestsch.zuoye.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 作业题目
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "zuoye_qestion")
public class ZuoyeQestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ZuoyeQestion.Update.class)
    private Integer id;

    @Column(name = "zuoye_id")
    private Integer zuoyeId;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "zuoye_id", insertable = false, updatable = false)
    private ZuoYe zuoye;


    @Column(name = "name")
    private String name;

    @Column(name = "config")
    private String config;

    @Column(name = "answer")
    private String answer;//无实际用处，供前端使用

    @Column(name = "description")
    private String description;

    @Column(name = "question_type_id")
    private Integer questionTypeId;

    /*@OneToOne
    @JoinColumn(name = "question_type_id", insertable = false, updatable = false)
    private QuestionType questionType;*/

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "file_name")
    private String fileName;//上传作业文件名

    public @interface Update {
    }

    ;
}
