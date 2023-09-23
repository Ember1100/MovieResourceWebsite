package com.panda.system.service.impl;

import com.panda.system.domin.CommentsInfo;
import com.panda.system.domin.CommentsReply;
import com.panda.system.domin.param.CommentParam;
import com.panda.system.domin.param.LikeNumParam;
import com.panda.system.domin.vo.CommentVo;
import com.panda.system.mapper.CommentsInfoMapper;
import com.panda.system.mapper.CommentsReplyMapper;
import com.panda.system.domin.mq.send.TopicSendMessage;
import com.panda.system.service.CommentsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class CommentsInfoServiceImpl implements CommentsInfoService {

    @Resource
    private CommentsInfoMapper commentsInfoMapper;

    @Resource
    private CommentsReplyMapper commentsReplyMapper;


    @Autowired
    TopicSendMessage topicSendMessage;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void save(CommentParam param) {
        if (redisTemplate.hasKey(param.getOwnerId() + "comment")) {
            redisTemplate.delete(param.getOwnerId() + "comment");
        }
        CommentsInfo commentsInfo = null;
        CommentsReply commentsReply;
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = dateFormat.parse(param.getCreateTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (param.getReplyToId() == null && param.getCommentId() == null) {
            commentsInfo = new CommentsInfo();
            //评价资源
            commentsInfo.setType((byte) 2);
            commentsInfo.setOwnerId(param.getOwnerId());
            commentsInfo.setFromId(param.getFromId());
            commentsInfo.setFromName(param.getFromName());
            commentsInfo.setFromAvatar(param.getFromAvatar());
            commentsInfo.setContent(param.getContent());
            commentsInfo.setCreateTime(date);
            commentsInfo.setUpdateTime(date);
            commentsInfoMapper.insertSelective(commentsInfo);
        } else {
            //回复他人
            commentsReply = new CommentsReply();
            commentsReply.setCommentId(param.getCommentId());
            commentsReply.setReplyCreateTime(date);
            commentsReply.setReplyFromId(param.getFromId());
            commentsReply.setReplyFromName(param.getFromName());
            commentsReply.setReplyFromAvatar(param.getFromAvatar());
            commentsReply.setReplyToId(param.getReplyToId());
            commentsReply.setReplyToName(param.getReplyToName());
            commentsReply.setReplyToAvatar(param.getReplyToAvatar());
            commentsReply.setReplyContent(param.getContent());
            commentsReply.setReplyUpdateTime(date);
            commentsReplyMapper.insertSelective(commentsReply);
        }
        topicSendMessage.send(param);
    }

    @Override
    public void changeLikeNum(LikeNumParam param) {
        CommentsInfo commentsInfo = commentsInfoMapper.selectByPrimaryKey(param.getId());
        Integer oldLike = commentsInfo.getLikeNum();
        Date updateTime = null;
        try {
            updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(param.getCreateTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (param.getIsLike()) {
            commentsInfo.setLikeNum(oldLike + 1);
        } else {
            commentsInfo.setLikeNum(oldLike - 1);
        }
        commentsInfo.setUpdateTime(updateTime);
        commentsInfoMapper.updateByPrimaryKeySelective(commentsInfo);
    }

    @Override
    public List<CommentVo> findByOwnerId(Long ownerId) {
        return commentsInfoMapper.findByOwnerId(ownerId);
    }


}
