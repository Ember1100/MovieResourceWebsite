package com.panda.web.form;


import lombok.Data;

@Data
public class CommentsInfoForm {
    private static final long serialVersionUID = 1690164381030501261L;


    private Integer type;

    //被评论者的id
    private String ownerId;


    private String pid;

    //评论者id

    private String fromId;

    //评论者名字
    private String fromName;

    //评论者id
    private String toId;

    //评论者名字
    private String toName;

    //获得点赞的数量
    private Integer likeNum = 0;

    //评论内容
    private String content;
}
