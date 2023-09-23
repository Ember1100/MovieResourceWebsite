package com.panda.web.controller.system;

import com.panda.common.response.ResponseResult;
import com.panda.system.domin.SysMovie;
import com.panda.system.domin.WatchMovieLog;
import com.panda.system.domin.mq.enitty.WatchRecordEntity;
import com.panda.system.domin.vo.SysMovieVo;
import com.panda.system.service.CommentsInfoService;
import com.panda.system.service.EsService;
import com.panda.system.service.SysMovieService;
import com.panda.system.service.WatchMovieLogService;
import com.panda.web.controller.BaseController;
import com.panda.system.domin.mq.send.TopicSendMessage;
import com.panda.web.param.UploadResponse;
import com.panda.web.utils.MinioUtil;
import com.panda.web.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;


@RestController
@Slf4j
public class SysMovieController extends BaseController {

    @Autowired
    private SysMovieService sysMovieService;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WatchMovieLogService watchMovieLogService;

    @Autowired
    TopicSendMessage topicSendMessage;

    @Autowired
    private EsService esService;

    /**
     * 搜索电影
     *
     * @param sysMovieVo
     * @return
     * @throws IOException
     */
    @GetMapping("/sysMovie/search")
    public ResultData searchMovies(SysMovieVo sysMovieVo) throws IOException {
        return ResultData.ok(esService.searchSysMovieLike(sysMovieVo.getMovieName()));
    }

    @GetMapping ("/sysMovie/getLunbotu")
    ResultData getLunbotu() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Local HostAddress: "+addr.getHostAddress());
        String hostname = addr.getHostName();
        System.out.println("Local host name: "+hostname);
        return  ResultData.ok(sysMovieService.getLunbotu());
    }

    @GetMapping("/sysMovie/find")
    public ResponseResult findAllMovies(SysMovieVo sysMovieVo) {
        startPage();
        List<SysMovie> data = sysMovieService.findAllMovies(sysMovieVo);
        return getResult(data);
    }

    @GetMapping("/sysMovie/findMovie")
    public ResponseResult findMovie(SysMovieVo sysMovieVo) {
        startPage();
        List<SysMovie> data = sysMovieService.findAllMovie(sysMovieVo);
        return getResult(data);
    }

    @GetMapping("/sysMovie/findVipMovie")
    public ResponseResult findVipMovie(SysMovieVo sysMovieVo) {
        startPage();
        List<SysMovie> data = sysMovieService.findVipMovie(sysMovieVo);
        return getResult(data);
    }

    @GetMapping("/sysMovie/findNewestMovie")
    public ResponseResult findNewestMovie(SysMovieVo sysMovieVo) {
        startPage();
        List<SysMovie> data = sysMovieService.findNewestMovie(sysMovieVo);
        return getResult(data);
    }

    @GetMapping("/sysMovie/find/{id}")
    public ResponseResult findMovieById(@PathVariable Long id) {
        return getResult(sysMovieService.findMovieById(id));
    }

    @PostMapping("/sysMovie/delete/{id}")
    public ResponseResult deleteMovieById(@PathVariable Long id) {
        return getResult(sysMovieService.deleteMovie(id));
    }

    @PostMapping("/sysMovie")
    public ResponseResult addMovie(@Validated @RequestBody SysMovie sysMovie) {
        return getResult(sysMovieService.addMovie(sysMovie));
    }

    @PutMapping("/sysMovie")
    public ResponseResult updateMovie(@Validated @RequestBody SysMovie sysMovie) {
        System.out.println(sysMovie);
        return getResult(sysMovieService.updateMovie(sysMovie));
    }

    @DeleteMapping("/sysMovie/{ids}")
    public ResponseResult deleteMovie(@PathVariable Long[] ids) {
        return getResult(sysMovieService.deleteMovie(ids));
    }



    @PostMapping("/recordWatchMovie")
    public ResultData recordWatchMovie(@RequestBody WatchMovieLog watchMovieLog) {
        Date date = new Date();
        watchMovieLog.setWatchTime(date);
        WatchRecordEntity watchRecordEntity = new WatchRecordEntity();
        watchRecordEntity.setMovieArea(watchMovieLog.getMovieArea());
        watchRecordEntity.setMovieId(watchMovieLog.getMovieId());
        watchRecordEntity.setNum(1);
        watchMovieLogService.addRecord(watchMovieLog);
        topicSendMessage.send(watchRecordEntity);
        return ResultData.ok();
    }

    @GetMapping("/sysMovie/find/rankingList/{listId}")
    public ResultData findRankingList(@PathVariable Integer listId) {
        if (listId == 1) {
            if (!redisTemplate.hasKey("total")) {
                List<SysMovie> sysMovies = sysMovieService.totalBoxOfficeList();
                for (SysMovie sysMovie : sysMovies) {
                    redisTemplate.opsForZSet().add("total", sysMovie, sysMovie.getWatchNum());
                }
            }
            return ResultData.ok(redisTemplate.boundZSetOps("total").reverseRange(0L, 9L));
        } else if (listId == 2) {
            if (!redisTemplate.hasKey("domestic")) {
                List<SysMovie> sysMovies = sysMovieService.domesticBoxOfficeList();
                for (SysMovie sysMovie : sysMovies) {
                    redisTemplate.opsForZSet().add("domestic", sysMovie, sysMovie.getWatchNum());
                }
            }
            return ResultData.ok(redisTemplate.boundZSetOps("domestic").reverseRange(0L, 9L));
        } else if (listId == 3) {
            if (!redisTemplate.hasKey("foreign")) {
                List<SysMovie> sysMovies = sysMovieService.foreignBoxOfficeList();
                for (SysMovie sysMovie : sysMovies) {
                    redisTemplate.opsForZSet().add("foreign", sysMovie, sysMovie.getWatchNum());
                }
            }
            return ResultData.ok(redisTemplate.boundZSetOps("foreign").reverseRange(0L, 9L));
        } else {
            return new ResultData(200, "参数有问题");
        }
    }
    /**
     * 获取影片详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/sysMovie/getMovieInfo/{id}")
    public ResultData getMovieInfo(@PathVariable Long id) {
        return ResultData.ok(sysMovieService.getMovieInfoVo(id));
    }

    /**
     * 猜你新欢
     *
     * @param id
     * @return
     */
    @GetMapping("/sysMovie/getYourLike/{id}")
    public ResultData getYourLike(@PathVariable Long id) {
        return ResultData.ok(sysMovieService.getYourLike(id));
    }


}



