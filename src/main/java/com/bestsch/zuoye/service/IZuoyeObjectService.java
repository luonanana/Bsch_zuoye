package com.bestsch.zuoye.service;

import com.bestsch.openapi.BoaUser;
import com.bestsch.zuoye.model.ZuoYe;
import com.bestsch.zuoye.model.ZuoyeObject;

import java.util.List;

public interface IZuoyeObjectService {

    void saveAll(List<ZuoyeObject> zuoyeObject);
    List<ZuoyeObject> findAll(ZuoyeObject zuoyeObject);
}
