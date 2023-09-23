package com.panda.system.domin.mq.enitty;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class WatchRecordEntity implements Serializable {

    private String movieArea;

    private int num;

    private long movieId;
}
