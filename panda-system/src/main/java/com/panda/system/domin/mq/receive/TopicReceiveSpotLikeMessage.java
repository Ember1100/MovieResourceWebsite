package com.panda.system.domin.mq.receive;

import com.panda.system.domin.mq.enitty.SpotLikeMessage;
import com.panda.system.domin.vo.CommentVo;
import com.panda.system.service.CommentsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RabbitListener(queues = "spotLike.message")
@Slf4j
public class TopicReceiveSpotLikeMessage {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommentsInfoService infoService;


    @RabbitHandler
    public void process(SpotLikeMessage spotLikeMessage) {
        log.info("receive message:{}", spotLikeMessage);
        Set<CommentVo> set = redisTemplate.opsForZSet().range(spotLikeMessage.getOwnerId() + "comment", 0, -1);
        for (CommentVo commentVo : set) {
            if (commentVo.getId().equals(spotLikeMessage.getId())) {
                redisTemplate.opsForZSet().remove(spotLikeMessage.getOwnerId() + "comment", commentVo);
                if (spotLikeMessage.getIsLike()) {
                    commentVo.setLikeNum(commentVo.getLikeNum() + 1);
                } else {
                    commentVo.setLikeNum(commentVo.getLikeNum() - 1);
                }
                redisTemplate.opsForZSet().add(commentVo.getOwnerId() + "comment", commentVo, commentVo.getLikeNum());
                break;
            }
        }
    }
}
