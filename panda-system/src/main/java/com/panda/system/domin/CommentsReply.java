package com.panda.system.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentsReply implements Serializable {
    private Long replyId;

    private Long ownerId;

    private Long commentId;

    private Long replyFromId;

    private String replyFromName;

    private String replyFromAvatar;

    private Long replyToId;

    private String replyToName;

    private String replyToAvatar;

    private String replyContent;

    private Date replyCreateTime;

    private Date replyUpdateTime;

    private static final long serialVersionUID = 1L;


}