package com.panda.system.domin.mq.enitty;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SpotLikeMessage implements Serializable {

    private Long userId;

    private Long id;

    private Long ownerId;

    private Boolean isLike;
}
