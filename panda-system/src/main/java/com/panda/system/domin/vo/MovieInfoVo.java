package com.panda.system.domin.vo;

import lombok.Data;


import java.io.Serializable;
import java.util.List;



@Data
public class MovieInfoVo implements Serializable {
    private String movieName;

    private String movieArea;
    //电影简介
    private String movieIntroduction;

    private String movieCategoryName;
    private List<CategoryVo> categoryVoList;
    private int playNum;
    private static final long serialVersionUID = 1L;

}
