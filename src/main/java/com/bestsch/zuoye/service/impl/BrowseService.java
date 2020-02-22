package com.bestsch.zuoye.service.impl;

import com.bestsch.zuoye.dao.BrowseRepository;
import com.bestsch.zuoye.model.Browse;
import com.bestsch.zuoye.service.IBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrowseService implements IBrowseService {
    @Autowired
    private BrowseRepository browseRepository;
    @Override
    public void save(Browse browse) {
        browseRepository.save(browse);
    }

    @Override
    public List<Browse> findByExplainId(Integer expId) {
        return browseRepository.findByExplainId(expId);
    }

}
