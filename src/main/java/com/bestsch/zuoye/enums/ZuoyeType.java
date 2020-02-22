package com.bestsch.zuoye.enums;

public enum ZuoyeType {

    RCRW_TYPE(1,"日常任务"),
    TBLX_TYPE(2,"同步练习"),
    DYJC_TYPE(3,"单元检测");


    private int id;
        private String name;

    ZuoyeType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
