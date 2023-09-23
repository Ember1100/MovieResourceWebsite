package com.panda.system.domin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserServiceDetailVo {

    private String userName;

    private Date serviceEndTime;

    private Integer surplusDay;

    private String status;

}
