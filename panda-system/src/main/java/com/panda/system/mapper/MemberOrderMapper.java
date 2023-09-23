package com.panda.system.mapper;

import com.panda.system.domin.MemberOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface MemberOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(MemberOrder record);

    int insertSelective(MemberOrder record);

    MemberOrder selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(MemberOrder record);

    int updateByPrimaryKey(MemberOrder record);

    List<MemberOrder> getMemberOrder(Long userId);

    Integer getTotal();
    Integer getTotalByDate(Date startTime);
    BigDecimal getTotalMoney();
    BigDecimal getTotalMoneyByDate(Date startTime);

    List<MemberOrder> getAWeekTotal(Date startTime);
    List<MemberOrder> getAWeekMoney(Date startTime);
}