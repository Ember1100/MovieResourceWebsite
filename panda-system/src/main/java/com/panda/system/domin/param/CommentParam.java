package com.panda.system.domin.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class CommentParam implements Serializable {

    private Long ownerId;

    private Long userId;

    private Long fromId;

    private String fromName;

    private String fromAvatar;

    private String content;

    private Long replyToId;

    private String replyToName;

    private String replyToAvatar;

    private Long commentId;

    private String createTime;

    private String updateTime;

    private static final long serialVersionUID = 1690164381030501261L;
}
