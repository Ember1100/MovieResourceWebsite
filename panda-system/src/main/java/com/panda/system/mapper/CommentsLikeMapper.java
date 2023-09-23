package com.panda.system.mapper;

import com.panda.system.domin.CommentsLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentsLikeMapper {
    int deleteByPrimaryKey(Long likeId);

    int insert(CommentsLike record);

    int insertSelective(CommentsLike record);

    CommentsLike selectByPrimaryKey(Long likeId);

    int updateByPrimaryKeySelective(CommentsLike record);

    int updateByPrimaryKey(CommentsLike record);

    List<Long> selectCommentId(@Param("userId") Long userId, @Param("ownerId") Long ownerId);

    CommentsLike selectCommentsLike(@Param("userId") Long userId,@Param("commentId") Long commentId);
}