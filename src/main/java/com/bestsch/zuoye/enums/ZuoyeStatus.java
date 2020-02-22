package com.bestsch.zuoye.enums;

public enum ZuoyeStatus {

    UNREVIEW(0,"未批"),
    REVIEW(1,"已批");


    private int id;
        private String name;

    ZuoyeStatus(int id, String name) {
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
