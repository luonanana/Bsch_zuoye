package com.bestsch.zuoye.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 作业讲解
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "explains")
public class Explain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Explain.Update.class)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "grade_id")
    private Integer gradeId;//年级ID

    @Column(name = "subject_id")
    private String subjectId;//学科ID

    @Column(name = "chapter_id")
    private Integer chapterId;//知识章节ID

    @Column(name = "video_name")
    private String videoName;//视频名

    @Column(name = "status", columnDefinition = "tinyint default 0 comment ' 0:未发布 1:已发布'")
    private Integer status;//是否发布 0-未发布  1-已发布

    // 视屏中中间截图，包含时间戳后缀
    @Column(name = "video_Picture")
    private String videoPicture;
    //视频大小
    @Column(name = "video_time")
    private Long videoTime;

    @Column(name = "c_id")
    private Integer cId;

    @Column(name = "c_name")
    private String cName;

    @Column(name = "c_date")
    private Date cDate;

    public @interface Update {
    }

    ;
}
