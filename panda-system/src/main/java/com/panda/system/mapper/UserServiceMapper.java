package com.panda.system.mapper;

import com.panda.system.domin.UserService;
import com.panda.system.domin.vo.UserServiceDetailVo;

import java.util.Date;
import java.util.List;

public interface UserServiceMapper {
    int deleteByPrimaryKey(Long serviceId);

    int insert(UserService record);

    int insertSelective(UserService record);

    UserService selectByPrimaryKey(Long serviceId);

    int updateByPrimaryKeySelective(UserService record);

    int updateByPrimaryKey(UserService record);
    int updateByUserId(UserService record);

    UserService selectByUserId(Long userId);

    UserServiceDetailVo getVipServiceDetail(Long userId);

    List<UserService> getVipUser();
}