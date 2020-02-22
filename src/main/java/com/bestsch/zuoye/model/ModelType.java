package com.bestsch.zuoye.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *模板类型
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "model_type")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ModelType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ModelType.Update.class)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "p_id")
    private Integer pId;

    @Transient
    private List<ModelType> children;

    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "modelType")
    @Transient
    private List<ZuoyeModel> zuoyeModels;


    public @interface Update{};
}
