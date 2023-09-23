package com.panda.system.service;

import com.panda.system.domin.CommentsReply;

import java.util.List;

public interface CommentsReplyService {
    /**
     * 保存评论回复
     * @param reply
     * @return
     */
    CommentsReply save(CommentsReply reply);

    /**
     * 根据评论id查询回复
     * @param commentId
     * @return
     */
    List<CommentsReply> findByCommentId(String commentId);


}
