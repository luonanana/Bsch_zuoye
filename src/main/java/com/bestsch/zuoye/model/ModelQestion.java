package com.bestsch.zuoye.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *模板题目
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "model_qestion")
public class ModelQestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ModelQestion.Update.class)
    private Integer id;

    @Column(name = "zuoye_model_id")
    private Integer zuoyeModelId;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.PERSIST},optional=false)
    @JoinColumn(name="zuoye_model_id", insertable = false,updatable = false)
    private ZuoyeModel zuoyeModel;


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


    @Column(name = "order_num")
    private Integer orderNum;


    public @interface Update{};
}
