package com.panda.system.service;

import com.panda.system.domin.param.LikeNumParam;

import java.util.List;

public interface CommentsLikeService {
   void addLikeRecord(LikeNumParam param);

   List<Long> getLikeCommentId(Long userId,Long ownerId);
}
