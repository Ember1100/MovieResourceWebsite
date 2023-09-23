package com.panda.system.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class SysMovieCategory implements Serializable {
    //序列号
    private static final long serialVersionUID = 1L;
    //电影分类id
    @Field(type = FieldType.Integer, store = true)
    private Long movieCategoryId;
    //电影分类名称
    @NotBlank(message = "电影分类名称不能为空")
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String movieCategoryName;

}
