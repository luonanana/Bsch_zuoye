package com.bestsch.zuoye.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.security.auth.Subject;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 *作业
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "zuoye")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ZuoYe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ZuoYe.Update.class)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;//作业要求

    @Column(name = "grade_id")
    private Integer gradeId;//年级ID

    @Column(name = "subject_id")
    private String subjectId;//学科ID

    @Column(name = "chapter_id")
    private Integer chapterId;//知识章节ID

    @Column(name = "zuoye_type_id")
    private Integer zuoyeTypeId;//作业类型

    @Column(name = "type_id")
    private Integer typeId;//区分推送对象 1-班级  2-学生

    @Column(name = "zuoye_model_id")
    private Integer zuoyeModelId;

    @OneToOne
    @JoinColumn(name="zuoye_model_id", insertable = false,updatable = false)
    private ZuoyeModel zuoyeModel;

    @Column(name = "start_date")
    private Date startDate;//发布时间

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private Integer status;//是否发布 1-已发布 0-未发布

    @Column(name = "c_id")
    private Integer cId;

    @Column(name = "c_name")
    private String cName;

    @Column(name = "c_date")
    private Date cDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "zuoye")
    private List<ZuoyeQestion> zuoyeQestionList;

   /* @Transient
    private Integer state;// 0-已参与， 1-未参与
*/
    public @interface Update{};
}
