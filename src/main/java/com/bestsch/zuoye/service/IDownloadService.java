package com.bestsch.zuoye.service;



import com.bestsch.zuoye.model.Download;

import java.util.List;

public interface IDownloadService {
    void save(Download download);
    List<Download> findByExplainId(Integer expId);
}
