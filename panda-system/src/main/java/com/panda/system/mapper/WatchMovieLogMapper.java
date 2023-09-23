package com.panda.system.mapper;

import com.panda.system.domin.WatchMovieLog;

public interface WatchMovieLogMapper {
    int deleteByPrimaryKey(Long watchId);

    int insert(WatchMovieLog record);

    int insertSelective(WatchMovieLog record);

    WatchMovieLog selectByPrimaryKey(Long watchId);

    int updateByPrimaryKeySelective(WatchMovieLog record);

    int updateByPrimaryKey(WatchMovieLog record);
}