package com.panda.system.service;

import com.panda.system.domin.MovieLunbotu;
import com.panda.system.domin.SysMovie;
import com.panda.system.domin.vo.MovieInfoVo;
import com.panda.system.domin.vo.SysMovieVo;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.List;


public interface SysMovieService {

    List search(SysMovieVo sysMovieVo) throws IOException;

    List<SysMovie> findAllMovies(SysMovieVo  sysMovieVo);

    List<SysMovie> findAllMovie(SysMovieVo sysMovieVo);

    List<SysMovie> findVipMovie(SysMovieVo sysMovieVo);

    List<SysMovie> findNewestMovie(SysMovieVo sysMovieVo);

    SysMovie findMovieById(Long id);

    SysMovie findOneMovie(Long id);

    int addMovie(SysMovie sysMovie);

    int updateMovie(SysMovie sysMovie);

    int deleteMovie(Long[] ids);
    int deleteMovie(Long id);


    List<MovieLunbotu> getLunbotu();

    /**
     * 总热播榜
     * @return
     */
    List<SysMovie> totalBoxOfficeList();

    /**
     * 国内热播榜
     * @return
     */
    List<SysMovie> domesticBoxOfficeList();

    /**
     * 国外热播榜
     * @return
     */
    List<SysMovie> foreignBoxOfficeList();


    MovieInfoVo getMovieInfoVo(Long id);


    List<SysMovie> getYourLike(Long id);

}
