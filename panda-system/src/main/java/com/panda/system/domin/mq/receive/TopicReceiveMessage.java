package com.panda.system.domin.mq.receive;

import com.panda.system.domin.SysMovie;
import com.panda.system.domin.mq.enitty.WatchRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RabbitListener(queues = "topic.message")
@Slf4j
public class TopicReceiveMessage {

    @Autowired
    private RedisTemplate redisTemplate;


    @RabbitHandler
    public void process(WatchRecordEntity message) {
        if (message.getMovieArea() == null) {
            return;
        }
        log.info("Topic Receiver  :{}", message);
        Set<SysMovie> list = redisTemplate.boundZSetOps("total").range(0L, 50l);
        if (message.getMovieArea().equals("中国大陆")) {
            for (SysMovie sysMovie : list) {
                if (sysMovie.getMovieId().longValue() == message.getMovieId()) {
                    redisTemplate.opsForZSet().remove("total", sysMovie);
                    redisTemplate.opsForZSet().remove("domestic", sysMovie);
                    sysMovie.setWatchNum(sysMovie.getWatchNum() + message.getNum());
                    redisTemplate.opsForZSet().add("total", sysMovie, sysMovie.getWatchNum());
                    redisTemplate.opsForZSet().add("domestic", sysMovie, sysMovie.getWatchNum());
                    break;
                }
            }
        } else {
            for (SysMovie sysMovie : list) {
                if (sysMovie.getMovieId().longValue() == message.getMovieId()) {
                    redisTemplate.opsForZSet().remove("total", sysMovie);
                    redisTemplate.opsForZSet().remove("foreign", sysMovie);
                    sysMovie.setWatchNum(sysMovie.getWatchNum() + message.getNum());
                    redisTemplate.opsForZSet().add("total", sysMovie, sysMovie.getWatchNum());
                    redisTemplate.opsForZSet().add("foreign", sysMovie, sysMovie.getWatchNum());
                    break;
                }

            }
        }
    }

}
