package com.panda.system.domin;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberOrder implements Serializable {
    private Long orderId;

    private Long userId;

    private Integer productId;

    private String productName;

    private Date createTime;

    private String payType;

    private BigDecimal payment;

    private String status;

    //商户订单编号
    private String orderNumber;
    private Long sign;

   private String payTimeStr;
   private Integer num;
}