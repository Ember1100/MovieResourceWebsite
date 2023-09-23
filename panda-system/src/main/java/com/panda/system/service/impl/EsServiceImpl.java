package com.panda.system.service.impl;

import com.panda.system.domin.SysMovie;
import com.panda.system.mapper.MovieRepository;
import com.panda.system.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ember
 */
@Service
public class EsServiceImpl implements EsService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<SysMovie> searchSysMovie(String key) {
        SearchPage<SysMovie> byMovieName = movieRepository.findByMovieName(key,PageRequest.of(0, 10));
        // 2. 高亮数据替换
        List<SearchHit<SysMovie>> searchHitList = byMovieName.getContent();
        ArrayList<SysMovie> sysMoviesDocList = new ArrayList<>(searchHitList.size());
        for (SearchHit<SysMovie> sysMovieSearchHit : searchHitList) {
            SysMovie content = sysMovieSearchHit.getContent();
            sysMoviesDocList.add(content);
        }
        return sysMoviesDocList;
    }

    @Override
    public List<SysMovie> searchSysMovieLike(String key) {
        SearchPage<SysMovie> movieSearchPage  = movieRepository.findByMovieNameLike(key,PageRequest.of(0, 1000));
        // 2. 高亮数据替换
        List<SearchHit<SysMovie>> searchHitList = movieSearchPage.getContent();
        ArrayList<SysMovie> sysMoviesDocList = new ArrayList<>(searchHitList.size());
        for (SearchHit<SysMovie> sysMovieSearchHit : searchHitList) {
            SysMovie content = sysMovieSearchHit.getContent();
            sysMoviesDocList.add(content);
        }
        return sysMoviesDocList;
    }
}
