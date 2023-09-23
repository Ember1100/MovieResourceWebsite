package com.panda.system.mapper;

import com.panda.system.domin.CommentsReply;

public interface CommentsReplyMapper {
    int deleteByPrimaryKey(Long replyId);

    int insert(CommentsReply record);

    int insertSelective(CommentsReply record);

    CommentsReply selectByPrimaryKey(Long replyId);

    int updateByPrimaryKeySelective(CommentsReply record);

    int updateByPrimaryKey(CommentsReply record);

    Long getNewReplyId();
}