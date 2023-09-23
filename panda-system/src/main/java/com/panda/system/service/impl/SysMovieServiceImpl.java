package com.panda.system.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.common.utils.JedisPoolUtils;
import com.panda.system.domin.MovieLunbotu;
import com.panda.system.domin.SysMovie;
import com.panda.system.domin.vo.MovieInfoVo;
import com.panda.system.domin.vo.SysMovieVo;
import com.panda.system.mapper.MovieLunbotuMapper;
import com.panda.system.mapper.SysMovieMapper;
import com.panda.system.service.SysMovieService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

@Service
public class SysMovieServiceImpl implements SysMovieService {

    @Autowired
    private SysMovieMapper sysMovieMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private MovieLunbotuMapper movieLunbotuMapper;

    @Override
    public List search(SysMovieVo sysMovieVo) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("movie");
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        List list = new ArrayList();
        //匹配查询
        builder.query(QueryBuilders.matchQuery("movieName", sysMovieVo.getMovieName()));
        request.source(builder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }

    @Override
    public List<SysMovie> findAllMovies(SysMovieVo sysMovieVo) {
        return sysMovieMapper.findAllMovies(sysMovieVo);
    }

    @Override
    public List<SysMovie> findAllMovie(SysMovieVo sysMovieVo) {
        return sysMovieMapper.selectMovie(sysMovieVo.getMovieCategoryId());
    }

    @Override
    public List<SysMovie> findVipMovie(SysMovieVo sysMovieVo) {
        return sysMovieMapper.selectVipMovie(sysMovieVo.getMovieCategoryId());
    }

    @Override
    public List<MovieLunbotu> getLunbotu() {
        return movieLunbotuMapper.getLunbotu();
    }

    @Override
    public List<SysMovie> findNewestMovie(SysMovieVo sysMovieVo) {
        return sysMovieMapper.selectNewestMovie(sysMovieVo.getMovieCategoryId(), sysMovieVo.getStartDate(), sysMovieVo.getEndDate());
    }


    @Override
    public SysMovie findMovieById(Long id) {
        return sysMovieMapper.findMovieById(id);
    }

    @Override
    public SysMovie findOneMovie(Long id) {
        return sysMovieMapper.findOneMovie(id);
    }

    @Override
    public int addMovie(SysMovie sysMovie) {
        return sysMovieMapper.addMovie(sysMovie);
    }

    @Override
    public int updateMovie(SysMovie sysMovie) {
        return sysMovieMapper.updateMovie(sysMovie);
    }

    @Override
    public int deleteMovie(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysMovieMapper.deleteMovie(id);
        }
        return rows;
    }

    @Override
    public int deleteMovie(Long id) {
        return sysMovieMapper.deleteMovie(id);
    }

    @Override
    public List<SysMovie> totalBoxOfficeList() {

        return sysMovieMapper.totalBoxOfficeList();
    }


    @Override
    public List<SysMovie> domesticBoxOfficeList() {
        return sysMovieMapper.domesticBoxOfficeList();
    }


    @Override
    public MovieInfoVo getMovieInfoVo(Long id) {
        return sysMovieMapper.getMovieInfoVo(id);
    }

    @Override
    public List<SysMovie> foreignBoxOfficeList() {
        return sysMovieMapper.foreignBoxOfficeList();
    }

    @Override
    public List<SysMovie> getYourLike(Long id) {
        return sysMovieMapper.getYourLike(id);
    }
}
