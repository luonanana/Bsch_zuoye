package com.bestsch.zuoye.mem;

import com.bestsch.openapi.BoaGroup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class GroupMem {

    private long saveTime = 0;
    private static long GROUP_SAVE_TIMEOUT = 3000000;

    synchronized
    public BoaGroup parseSchoolGroup(BoaGroup boaGroup, List<BoaGroup> groups) {
        parseGroup(boaGroup, groups);
        parseGroup2(boaGroup);
        parseGroup2(boaGroup);
        parseGroup2(boaGroup);
        return boaGroup;
    }


    private boolean parseGroup(BoaGroup boaGroup, List<BoaGroup> groups) {

        if (boaGroup.getType() == GroupType.SCHOOL.id()) {
            boolean flag = false;
            if (groups != null && groups.size() != 0) {
                for (int i = 0; i < groups.size(); i++) {
                    if (groups.get(i).getId() == boaGroup.getId()) {
                        flag = true;
                    }
                }
            }
            boaGroup.setChildren(new ArrayList<>());
            return flag;
        }

        List<BoaGroup> children = boaGroup.getChildren();

        if (children != null && children.size() != 0) {
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).getType()!=GroupType.SCHOOL.id()&&(children.get(i).getChildren() == null || children.get(i).getChildren().size() == 0)) {
                    children.remove(i);
                    i--;
                    continue;
                }
                boolean res = parseGroup(children.get(i), groups);
                if (!res) {
                    children.remove(i);
                    i--;
                }
            }
        }
        return true;
    }

    private void parseGroup2(BoaGroup boaGroup) {

//        System.out.println(boaGroup.getName());
        if (boaGroup.getType() == GroupType.SCHOOL.id()) {
            return;
        }

        List<BoaGroup> children = boaGroup.getChildren();

        if (children != null && children.size() != 0) {
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).getType()!=GroupType.SCHOOL.id()&&(children.get(i).getChildren() == null || children.get(i).getChildren().size() == 0)) {
                    children.remove(i);
                    i--;
                    continue;
                }
                parseGroup2(children.get(i));
            }
        }
    }



}
