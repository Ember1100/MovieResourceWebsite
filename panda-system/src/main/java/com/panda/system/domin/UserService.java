package com.panda.system.domin;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserService implements Serializable {
    private Long serviceId;

    private Long userId;

    private Date serviceStartTime;

    private Date serviceEndTime;

    private Integer status;


    private int dayRemain;
    private Integer subNum;
    private String payTimeStr;


}