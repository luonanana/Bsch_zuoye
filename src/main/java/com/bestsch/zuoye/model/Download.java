package com.bestsch.zuoye.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *讲解下载量
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "download")
public class Download {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Download.Update.class)
    private Integer id;

    @Column(name = "explain_id")
    private Integer explainId;


    @Column(name = "u_id")
    private Integer uId;

    @Column(name = "u_name")
    private String uName;

    @Column(name = "u_date")
    private Date uDate;

    public @interface Update{};
}
