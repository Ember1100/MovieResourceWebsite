package com.panda.system.service;

import com.github.pagehelper.PageInfo;
import com.panda.system.domin.MemberOrder;
import com.panda.system.domin.param.OrderParam;
import com.panda.system.domin.param.PayOrderParam;
import com.panda.system.domin.vo.UserServiceDetailVo;

import java.util.List;

public interface MemberOrderService {

    /**
     * 购买会员
     * @param param
     * @return
     */
    int addVipServiceRecord(PayOrderParam param);

    PageInfo<MemberOrder> getUserMemberOrder(OrderParam param);

    UserServiceDetailVo getUserServiceDetail(Long userId);

}
