package com.bestsch.zuoye.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


/**
 *作业模板
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "zuoye_model")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ZuoyeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ZuoyeModel.Update.class)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "descripe")
    private String descripe;

    @Column(name = "model_type_id")
    private Integer modelTypeId;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name="model_type_id", insertable = false,updatable = false)
    private ModelType modelType;


    @Column(name = "c_id")
    private Integer cId;

    @Column(name = "c_name")
    private String cName;

    @Column(name = "c_date")
    private Date cDate;

    @Transient
    private Integer useCount=0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "zuoyeModel")
    private List<ModelQestion> zuoyeQestionList;


    public @interface Update{};
}
