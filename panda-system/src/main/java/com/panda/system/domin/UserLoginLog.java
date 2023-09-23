package com.panda.system.domin;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserLoginLog implements Serializable {
    private Long loginId;

    private Long userId;

    private Date loginTime;

    private Integer num;

    private String payTimeStr;

}