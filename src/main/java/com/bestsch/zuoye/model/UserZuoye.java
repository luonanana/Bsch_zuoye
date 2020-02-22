package com.bestsch.zuoye.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 *用户作业
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_zuoye")
public class UserZuoye {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = UserZuoye.Update.class)
    private Integer id;


    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "class_name")
    private String className;

   /* @Column(name = "school_id")
    private Integer schoolId;

    @Column(name = "school_name")
    private String schoolName;*/

    @Column(name = "zuoye_id")
    private Integer zuoyeId;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY)
    @JoinColumn(name="zuoye_id", insertable = false,updatable = false)
    private ZuoYe zuoye;


    @Column(name = "date")
    private Date date;

    @Column(name = "status")
    private Integer status;//是否批阅 1-已批阅 0-未批阅

    @Column(name = "appraisal")
    private String appraisal;//评价

    /*@Transient
    private List<ZuoyeQestion> zuoyeQestion;*/

    @Transient
    private double totalScore;
    public @interface Update{};
}
