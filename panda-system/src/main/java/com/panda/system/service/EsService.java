package com.panda.system.service;

import com.panda.system.domin.SysMovie;

import java.util.List;

/**
 * @author ember
 */
public interface EsService {

    /**
     * 输入名字查找
     * @param key
     * @return
     */
    List<SysMovie> searchSysMovie(String key);


    /**
     * 模糊查询
     * @param key
     * @return
     */
    List<SysMovie> searchSysMovieLike(String key);
}
