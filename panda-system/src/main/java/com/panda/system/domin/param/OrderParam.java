package com.panda.system.domin.param;

import lombok.Data;

@Data
public class OrderParam {
    private Integer pageSize;
    private Integer pageNum;
    private Long userId;
}
