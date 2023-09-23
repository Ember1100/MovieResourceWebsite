package com.panda.system.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentsInfo  implements Serializable{
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

    private static final long serialVersionUID = 1L;



}