package com.panda.system.domin.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LikeNumParam {
    private Long userId;
    private Long id;
    private Integer type;
    private String createTime;
    private Long ownerId;
    private Boolean isLike;

}
