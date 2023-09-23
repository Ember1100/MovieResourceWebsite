package com.panda.system.service;

import com.panda.system.domin.CommentsInfo;
import com.panda.system.domin.param.CommentParam;
import com.panda.system.domin.param.LikeNumParam;
import com.panda.system.domin.vo.CommentVo;

import java.util.List;
import java.util.Set;

public interface CommentsInfoService {
    /**
     * 保存评论
     * @param info
     * @return
     */
    void save(CommentParam info);

    /**
     * 根据被评论者的id查询评论列表
     * @param ownerId
     * @return
     */
    List<CommentVo> findByOwnerId(Long ownerId);

    void changeLikeNum(LikeNumParam param);





}
