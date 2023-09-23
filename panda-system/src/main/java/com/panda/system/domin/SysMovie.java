package com.panda.system.domin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "movie",createIndex = false)
public class SysMovie implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    private Long movieId;

    //电影名称
    @NotBlank(message = "电影名称不能为空")
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String movieName;

    //电影时长
    @Field(type = FieldType.Integer, store = true)
    private Integer movieLength;

    //电影海报
    @Field(type = FieldType.Keyword, store = true)
    private String moviePoster;

    @Field(type = FieldType.Keyword, store = true)
    private String movieArea;

    //上映日期
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releaseDate;

    @Field(type = FieldType.Double, store = true)
    //电影总票房
    private Double movieBoxOffice;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    //电影简介
    private String movieIntroduction;

    //电影图集
    @Field(type = FieldType.Keyword, store = true)
    private String moviePictures;

    //电影的类别
    @Field(type = FieldType.Nested, store = true,index = true)
    private List<SysMovieCategory> movieCategoryList;

    @Field(type = FieldType.Keyword, store = true)
    private String filmUrl;

    //是否免费看
    private int isFreeWatch;

    @Field(type = FieldType.Integer, store = true)
    private int watchNum;
}
