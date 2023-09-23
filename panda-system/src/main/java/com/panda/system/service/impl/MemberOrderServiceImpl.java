package com.panda.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.common.page.PageBuilder;
import com.panda.system.domin.MemberOrder;
import com.panda.system.domin.SysUser;
import com.panda.system.domin.UserService;
import com.panda.system.domin.param.OrderParam;
import com.panda.system.domin.param.PayOrderParam;
import com.panda.system.domin.vo.SysMovieVo;
import com.panda.system.domin.vo.UserServiceDetailVo;
import com.panda.system.mapper.MemberOrderMapper;
import com.panda.system.mapper.SysUserMapper;
import com.panda.system.mapper.UserLoginLogMapper;
import com.panda.system.mapper.UserServiceMapper;
import com.panda.system.service.MemberOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MemberOrderServiceImpl implements MemberOrderService {

    @Autowired
    private MemberOrderMapper memberOrderMapper;

    @Autowired
    private UserServiceMapper userServiceMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public PageInfo<MemberOrder> getUserMemberOrder(OrderParam param) {
        PageHelper.startPage(param.getPageNum(),param.getPageSize());
        List<MemberOrder> list = memberOrderMapper.getMemberOrder(param.getUserId());
        PageInfo<MemberOrder> objectPageInfo = new PageInfo<>(list);
        return objectPageInfo;
    }

    @Override
    public UserServiceDetailVo getUserServiceDetail(Long userId) {
        UserServiceDetailVo userServiceDetail = userServiceMapper.getVipServiceDetail(userId);
        if (userServiceDetail.getServiceEndTime() == null || userServiceDetail.getSurplusDay() ==null) {
            userServiceDetail.setStatus("未开通");
            return userServiceDetail;
        }
        if (userServiceDetail.getSurplusDay() <= 0 ) {
            userServiceDetail.setStatus("已过期");
            userServiceDetail.setSurplusDay(0);
        } else if(userServiceDetail.getSurplusDay() > 0) {
            userServiceDetail.setStatus("已开通");
        }
        return userServiceDetail;
    }

    @Transactional
    @Override
    public int addVipServiceRecord(PayOrderParam param) {
        Calendar calendar = Calendar.getInstance();
        Date createTime = calendar.getTime();
        Date endTime;
        UserService userService = userServiceMapper.selectByUserId(param.getUserId());
        MemberOrder memberOrder = new MemberOrder();
        if (userService!=null) {
            calendar.setTime(userService.getServiceEndTime());
        }
        if (param.getProductId() == 1) { //1个月会员
            calendar.add(Calendar.MONTH,1);
        } else if (param.getProductId() == 2) { //3个月会员
            calendar.add(Calendar.MONTH,3);
        } else if (param.getProductId() == 3) {//6个月会员
            calendar.add(Calendar.MONTH,6);
        } else if (param.getProductId() == 4) {//1年会员
            calendar.add(Calendar.MONTH,12);
        }
        endTime = calendar.getTime();

        if (userService !=null) {
            userService.setServiceEndTime(endTime);
            userServiceMapper.updateByPrimaryKeySelective(userService);
        }else {
            UserService us = new UserService();
            us.setUserId(param.getUserId());
            us.setServiceStartTime(createTime);
            us.setServiceEndTime(endTime);
            userServiceMapper.insertSelective(us);
        }
        memberOrder.setSign(Long.valueOf(param.getSign()));
        memberOrder.setProductName(param.getProductName());
        memberOrder.setCreateTime(createTime);
        memberOrder.setProductId(param.getProductId());
        memberOrder.setPayment(param.getPrice());
        memberOrder.setPayType(param.getPayType());
        memberOrder.setUserId(param.getUserId());
        memberOrder.setOrderNumber(param.getOrderNumber());
        memberOrder.setStatus(param.getStatus());
        SysUser sysUser = new SysUser();
        sysUser.setUserId(param.getUserId());
        sysUser.setRoleId(2L);
//        sysUserMapper.update(sysUser);
        return  memberOrderMapper.insertSelective(memberOrder);
    }


}
