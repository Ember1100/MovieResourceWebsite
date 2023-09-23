package com.panda.system.service.impl;

import com.panda.system.domin.WatchMovieLog;
import com.panda.system.mapper.WatchMovieLogMapper;

import com.panda.system.service.WatchMovieLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchMovieLogServiceImpl implements WatchMovieLogService {

    @Autowired
    private WatchMovieLogMapper watchMovieLogMapper;

    @Override
    public int addRecord(WatchMovieLog watchMovieLog) {
        return watchMovieLogMapper.insertSelective(watchMovieLog);

    }
}
