package com.bestsch.zuoye.enums;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum GradeEnum {
   /* GRADE100("幼托",100),
    GRADE101("幼小",101),
    GRADE102("幼中",102),
    GRADE103("幼大",103),*/
    GRADE1("小学一年级上",1,1),
    GRADE2("小学一年级下",1,2),
    GRADE3("小学二年级上",2,1),
    GRADE4("小学二年级下",2,2),
    GRADE5("小学三年级上",3,1),
    GRADE6("小学三年级下",3,2),
    GRADE7("小学四年级上",4,1),
    GRADE8("小学四年级下",4,2),
    GRADE9("小学五年级上",5,1),
    GRADE10("小学五年级下",5,2),
    GRADE11("小学六年级上",6,1),
    GRADE12("小学六年级下",6,2),
    GRADE13("初中一年级上",7,1),
    GRADE14("初中一年级下",7,2),
    GRADE15("初中二年级上",8,1),
    GRADE16("初中二年级下",8,2),
    GRADE17("初中三年级上",9,1),
    GRADE18("初中三年级下",9,2),
    GRADE119("高中一年级上",10,1),
    GRADE20("高中一年级下",10,2),
    GRADE21("高中二年级上",11,1),
    GRADE22("高中二年级下",11,2),
    GRADE23("高中三年级上",12,1),
    GRADE24("高中三年级下",12,2), ;

    private String name;
    private Integer system_garde;
    private Integer term;

    GradeEnum(String name, Integer system_garde,Integer term) {
        this.name = name;
        this.system_garde = system_garde;
        this.term=term;
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


