package com.panda.system.domin;

import java.io.Serializable;
import java.util.Date;

public class MovieLunbotu implements Serializable {
    private Long lunbId;

    private Long movieId;

    private Integer status;

    private String picUrl;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getLunbId() {
        return lunbId;
    }

    public void setLunbId(Long lunbId) {
        this.lunbId = lunbId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}