package com.panda.system.mapper;

import com.panda.system.domin.UserLoginLog;

import java.util.Date;
import java.util.List;

public interface UserLoginLogMapper {
    int deleteByPrimaryKey(Long loginId);

    int insert(UserLoginLog record);

    int insertSelective(UserLoginLog record);

    UserLoginLog selectByPrimaryKey(Long loginId);

    int updateByPrimaryKeySelective(UserLoginLog record);

    int updateByPrimaryKey(UserLoginLog record);

    Integer getTotal();

    Integer getTotalByDate(Date startTime);

    List<UserLoginLog> getAWeekTotal(Date startTime);

}