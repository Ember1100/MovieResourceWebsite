package com.panda.web.controller.system;

import com.panda.system.domin.mq.enitty.SpotLikeMessage;
import com.panda.system.domin.param.LikeNumParam;
import com.panda.system.domin.vo.CommentVo;
import com.panda.system.service.CommentsInfoService;
import com.panda.system.domin.param.CommentParam;
import com.panda.system.service.CommentsLikeService;
import com.panda.system.domin.mq.send.TopicSendMessage;
import com.panda.web.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CommentController {
    @Autowired
    private CommentsInfoService infoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommentsLikeService commentsLikeService;

    @Autowired
    TopicSendMessage topicSendMessage;

    /**
     * 点赞
     */
    @PostMapping("/addLikeRecord")
    public ResultData addLikeRecord(@RequestBody LikeNumParam param) {

        SpotLikeMessage spotLikeMessage = new SpotLikeMessage();
        spotLikeMessage.setId(param.getId());
        spotLikeMessage.setUserId(param.getUserId());
        spotLikeMessage.setOwnerId(param.getOwnerId());
        spotLikeMessage.setIsLike(param.getIsLike());
        //发送消息给redis 修改点赞数量
        topicSendMessage.send(spotLikeMessage);
        infoService.changeLikeNum(param);
        commentsLikeService.addLikeRecord(param);

        return ResultData.ok();
    }

    /**
     * 根据ownerId查询评论
     *
     * @param
     * @return
     */
    @PostMapping("/getCommentsByOwnerId")
    public ResultData getCommentsByOwnerId(@RequestBody CommentParam param) {
        Map<String, Object> map = new HashMap<>(20);
        map.put("ownerId", param.getOwnerId());
        Collection<CommentVo> set;
        if (redisTemplate.hasKey(param.getOwnerId() + "comment")) {
            set = redisTemplate.opsForZSet().reverseRange(param.getOwnerId() + "comment", 0, -1);
        }else {
            set = infoService.findByOwnerId(param.getOwnerId());
        }
        if (param.getUserId() == null) {
            map.put("list", set);
        } else {
            List<Long> likeCommentId = commentsLikeService.getLikeCommentId(param.getUserId(), param.getOwnerId());
            for (Long aLong : likeCommentId) {
                set.forEach(commentVo -> {
                    if (commentVo.getId().equals(aLong)) {
                        commentVo.setIsLike(true);
                    }
                });
            }
            map.put("list", set);
        }
        return ResultData.ok(map);

    }


    /**
     * 评论功能
     *
     * @return
     */
    @PostMapping("/comment")
    public ResultData comment(@RequestBody CommentParam param) {
        infoService.save(param);
        return ResultData.ok();
    }


}
