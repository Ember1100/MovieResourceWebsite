package com.panda.system.domin.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class PayOrderParam {

    private Integer productId;

    private String orderNumber;

    private String payType;
    private String qrCode;

    private String status;

    private String productName;

    private BigDecimal price;

    private Long userId;

    private String sign;


}
