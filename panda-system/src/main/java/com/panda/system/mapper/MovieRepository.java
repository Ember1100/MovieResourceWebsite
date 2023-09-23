package com.panda.system.mapper;

import co.elastic.clients.elasticsearch.ml.Page;
import org.springframework.data.domain.Pageable;
import com.panda.system.domin.SysMovie;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ember
 */
@Resource
public interface MovieRepository extends ElasticsearchRepository<SysMovie,Long> {


    SearchPage<SysMovie> findByMovieName(String movieName,Pageable pageable);

    SearchPage<SysMovie> findByMovieNameLike(String movieName,Pageable pageable);

}
