package com.bestsch.zuoye.service.impl;


import com.bestsch.zuoye.dao.DownloadRepository;

import com.bestsch.zuoye.model.Download;

import com.bestsch.zuoye.service.IDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadService implements IDownloadService {
    @Autowired
    private DownloadRepository downloadRepository;
    @Override
    public void save(Download download) {
        downloadRepository.save(download);
    }



    @Override
    public List<Download> findByExplainId(Integer expId) {
        return downloadRepository.findByExplainId(expId);
    }

}
