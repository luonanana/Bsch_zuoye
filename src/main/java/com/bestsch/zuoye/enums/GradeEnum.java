package com.bestsch.zuoye.enums;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum GradeEnum {
    GRADE100("幼托",100),
    GRADE101("幼小",101),
    GRADE102("幼中",102),
    GRADE103("幼大",103),
    GRADE1("小学一年级",1),
    GRADE2("小学二年级",2),
    GRADE3("小学三年级",3),
    GRADE4("小学四年级",4),
    GRADE5("小学五年级",5),
    GRADE6("小学六年级",6),
    GRADE7("初中一年级",7),
    GRADE8("初中二年级",8),
    GRADE9("初中三年级",9),
    GRADE10("高中一年级",10),
    GRADE11("高中二年级",11),
    GRADE12("高中三年级",12),
    ;

    private String name;
    private Integer system_garde;

    GradeEnum(String name, Integer system_garde) {
        this.name = name;
        this.system_garde = system_garde;
    }

    private static List<GradeEnum> list;

    public static List<GradeEnum> list()
    {
        if (list != null) return list;

        list = new ArrayList<>();

        EnumSet<GradeEnum> statusSet = EnumSet.allOf(GradeEnum.class);
        for (GradeEnum status : statusSet)
            list.add(status);

        return list;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSystem_garde() {
        return system_garde;
    }

    public void setSystem_garde(Integer system_garde) {
        this.system_garde = system_garde;
    }
}


