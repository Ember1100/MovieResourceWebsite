package com.panda.system.mapper;

import com.panda.system.domin.MovieLunbotu;

import java.util.List;

public interface MovieLunbotuMapper {
    int deleteByPrimaryKey(Long lunbId);

    int insert(MovieLunbotu record);

    int insertSelective(MovieLunbotu record);

    MovieLunbotu selectByPrimaryKey(Long lunbId);

    int updateByPrimaryKeySelective(MovieLunbotu record);

    int updateByPrimaryKey(MovieLunbotu record);

    List<MovieLunbotu> getLunbotu();
}