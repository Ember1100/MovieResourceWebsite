package com.panda.system.service.impl;

import com.panda.system.domin.CommentsLike;
import com.panda.system.domin.param.LikeNumParam;
import com.panda.system.mapper.CommentsLikeMapper;
import com.panda.system.service.CommentsLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommentsLikeServiceImpl implements CommentsLikeService {

    @Autowired
    private CommentsLikeMapper commentsLikeMapper;

    @Override
    public void addLikeRecord(LikeNumParam param) {
        CommentsLike like = commentsLikeMapper.selectCommentsLike(param.getUserId(), param.getId());
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(param.getCreateTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (like == null) {
            CommentsLike commentsLike = new CommentsLike();
            commentsLike.setCommentId(param.getId());
            commentsLike.setType(0);
            commentsLike.setCreateTime(date);
            commentsLike.setUserId(param.getUserId());
            commentsLike.setUpdateTime(new Date());
            commentsLike.setOwnerId(param.getOwnerId());
            commentsLikeMapper.insertSelective(commentsLike);
        }else {
            if (param.getIsLike()) {
                like.setType(0);
            }else {
                like.setType(1);
            }
            like.setUpdateTime(date);
            commentsLikeMapper.updateByPrimaryKeySelective(like);
        }
    }

    @Override
    public List<Long> getLikeCommentId(Long userId,Long ownerId) {
        return commentsLikeMapper.selectCommentId(userId,ownerId);
    }
}
