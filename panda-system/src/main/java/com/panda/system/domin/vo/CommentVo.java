package com.panda.system.domin.vo;

import com.panda.system.domin.CommentsInfo;
import com.panda.system.domin.CommentsReply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo implements Serializable {

    private Long id;

    private Byte type;

    private Long ownerId;

    private Long fromId;

    private String fromName;

    private String fromAvatar;

    private int likeNum;

    private String content;

    private Date createTime;

    private Date updateTime;

    private Boolean isLike = false;

    private List<CommentsReply> list = new ArrayList<>();


    public CommentVo(CommentsInfo commentsInfo) {
        this.type = commentsInfo.getType();
        this.ownerId = commentsInfo.getOwnerId();
        this.fromId = commentsInfo.getFromId();
        this.fromName = commentsInfo.getFromName();
        this.fromAvatar = commentsInfo.getFromAvatar();
        this.likeNum = commentsInfo.getLikeNum();
        this.content = commentsInfo.getContent();
        this.createTime = commentsInfo.getCreateTime();
        this.updateTime = commentsInfo.getUpdateTime();
    }


}
