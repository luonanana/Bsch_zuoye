package com.bestsch.zuoye.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *题库题目
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Question.Update.class)
    private Integer id;

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

    @OneToOne
    @JoinColumn(name="question_type_id", insertable = false,updatable = false)
    private QuestionType questionType;

    @Column(name = "c_id")
    private Integer cId;

    @Column(name = "c_name")
    private String cName;

    @Column(name = "c_date")
    private Date cDate;

    public @interface Update{};
}
