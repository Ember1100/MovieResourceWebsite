package com.panda.system.service.impl;

import com.panda.system.domin.CommentsReply;
import com.panda.system.mapper.CommentsReplyMapper;
import com.panda.system.service.CommentsReplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CommentsReplyServiceImpl implements CommentsReplyService {

    @Resource
    private CommentsReplyMapper commentsReplyMapper;

    @Override
    public CommentsReply save(CommentsReply reply) {
        commentsReplyMapper.insertSelective(reply);
        return reply;
    }

    @Override
    public List<CommentsReply> findByCommentId(String commentId) {
        return null;
    }
}
