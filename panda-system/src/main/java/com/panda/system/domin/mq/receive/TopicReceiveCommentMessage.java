package com.panda.system.domin.mq.receive;


import com.panda.system.domin.CommentsInfo;
import com.panda.system.domin.param.CommentParam;
import com.panda.system.domin.vo.CommentVo;
import com.panda.system.mapper.CommentsInfoMapper;
import com.panda.system.service.CommentsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RabbitListener(queues = "comment.message")
@Slf4j
public class TopicReceiveCommentMessage {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommentsInfoService infoService;

    /**
     * 更新redis评论信息
     *
     * @param message
     */
    @RabbitHandler
    public void process(CommentParam message) {
        log.info("Topic Receiver  :{}", message);
        if (!redisTemplate.hasKey(message.getOwnerId() + "comment")) {
            List<CommentVo> list = infoService.findByOwnerId(message.getOwnerId());
            if (list.size() > 0) {
                for (CommentVo c : list) {
                    redisTemplate.opsForZSet().add(message.getOwnerId() + "comment", c, c.getLikeNum());
                }
            }
        }
    }


}
