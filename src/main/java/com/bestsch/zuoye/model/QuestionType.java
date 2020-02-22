package com.bestsch.zuoye.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *题目类型
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "question_type")
public class QuestionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = QuestionType.Update.class)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "c_id")
    private Integer cId;

    @Column(name = "c_name")
    private String cName;

    @Column(name = "c_date")
    private Date cDate;

    public @interface Update{};
}
