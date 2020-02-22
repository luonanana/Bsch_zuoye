package com.bestsch.zuoye.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *作业开放对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "zuoye_object")
public class ZuoyeObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ZuoyeObject.Update.class)
    private Integer id;

    @Column(name = "zuoye_id")
    private Integer zuoyeId;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY)
    @JoinColumn(name="zuoye_id", insertable = false,updatable = false)
    private ZuoYe zuoye;


    @Column(name = "cla_id")
    private Integer claId;

    @Column(name = "stu_id")
    private Integer stuId;


    public @interface Update{};
}
