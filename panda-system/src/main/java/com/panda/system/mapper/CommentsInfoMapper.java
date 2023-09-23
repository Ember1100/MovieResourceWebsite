package com.panda.system.mapper;

import com.panda.system.domin.CommentsInfo;
import com.panda.system.domin.vo.CommentVo;

import java.util.List;
import java.util.Set;

public interface CommentsInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CommentsInfo record);

    int insertSelective(CommentsInfo record);

    CommentsInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommentsInfo record);

    int updateByPrimaryKey(CommentsInfo record);

    List<CommentVo> findByOwnerId(Long ownerId);

    Long getNewInfoId();
}