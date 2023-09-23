package com.panda.system.service.impl;

import com.panda.system.domin.MemberOrder;
import com.panda.system.domin.UserLoginLog;
import com.panda.system.domin.UserService;
import com.panda.system.mapper.MemberOrderMapper;
import com.panda.system.mapper.SysUserMapper;
import com.panda.system.mapper.UserLoginLogMapper;
import com.panda.system.mapper.UserServiceMapper;
import com.panda.system.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private MemberOrderMapper memberOrderMapper;

    @Autowired
    private UserServiceMapper userServiceMapper;

    @Override
    public List<Object> getTotal(Date startTime) {
        List<Object> list = new ArrayList<>();
        Integer total1;
        //总用户量
        Integer total2 = sysUserMapper.getTotal();
        BigDecimal totalMoney;
        Integer total3;
        if (startTime == null) {
            //总访问量
            total1 = userLoginLogMapper.getTotal();
            //总用户量
            //总金额
            totalMoney = memberOrderMapper.getTotalMoney();
            //总订单数
            total3 = memberOrderMapper.getTotal();
        } else {
            //总访问量
            total1 = userLoginLogMapper.getTotalByDate(startTime);

            //总金额
            totalMoney = memberOrderMapper.getTotalMoneyByDate(startTime);
            //总订单数
            total3 = memberOrderMapper.getTotalByDate(startTime);
        }
        list.add(total1);
        list.add(total2);
        list.add(totalMoney);
        list.add(total3);
        return list;
    }

    @Override
    public Map getLineDate(Date startTime, String type) throws ParseException {
        Map m = new HashMap();
        List<String> dayList = getDayList();
        m.put("date", dayList);
        Map<String, Double> map = new TreeMap<>();
        List list = null;
        switch (type) {
            case "newVisitis":
                List<UserLoginLog> aWeekTotal1 = userLoginLogMapper.getAWeekTotal(startTime);
                for (String s : dayList) {
                    //一周访问量
                    UserLoginLog userLoginLog = aWeekTotal1.stream().filter(c -> c.getPayTimeStr().equals(s)).distinct().findAny().orElse(null);
                    if (userLoginLog != null && userLoginLog.getNum() != null) {
                        map.put(s, userLoginLog.getNum().doubleValue());
                    } else {
                        map.put(s, 0.0);
                    }
                }
                break;
            case "purchases":
                List<MemberOrder> aWeekMoney = memberOrderMapper.getAWeekMoney(startTime);
                for (String s : dayList) {
                    //一周金额
                    MemberOrder memberOrder = aWeekMoney.stream().filter(c -> c.getPayTimeStr().equals(s)).distinct().findAny().orElse(null);
                    if (memberOrder != null && memberOrder.getPayment() != null) {
                        map.put(s, memberOrder.getPayment().doubleValue());
                    } else {
                        map.put(s, 0.0);
                    }
                }
                break;
            case "shoppings": //一周下单量
                List<MemberOrder> aWeekTotal2 = memberOrderMapper.getAWeekTotal(startTime);
                for (String s : dayList) {
                    MemberOrder memberOrder2 = aWeekTotal2.stream().filter(c -> c.getPayTimeStr().equals(s)).distinct().findAny().orElse(null);
                    if (memberOrder2 != null && memberOrder2.getNum() != null) {
                        map.put(s, memberOrder2.getNum().doubleValue());
                    } else {
                        map.put(s, 0.0);
                    }
                }
                break;
            case "users":
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                List<UserService> vipUser = userServiceMapper.getVipUser();
                List<String> collect = vipUser.stream().map(UserService::getPayTimeStr).collect(Collectors.toList());
                List<String> user = sysUserMapper.getUser();
                list = new ArrayList();
                for (String s : dayList) {
                    int sum =0;
                    int num = 0;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(format.parse(s));
                    long l = calendar.getTime().getTime();
                    for (String ss : collect) {
                        calendar.setTime(format.parse(ss));
                        long time = calendar.getTime().getTime();
                        if (time-l <=0) {
                            sum = sum+1;
                        }
                    }
                    list.add(sum);
                    for (String sss : user) {
                        calendar.setTime(format.parse(sss));
                        long time = calendar.getTime().getTime();
                        if (time-l <=0) {
                            num = num+1;
                        }
                    }
                    map.put(s, (double) num);
                }
                break;
        }
        List collect = new ArrayList(map.values());
        m.put("actualData", collect);
        if (list != null && list.size() > 0) {
            m.put("expectedData", list);
        }
        return m;
    }


    public List<String> getDayList() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        LinkedList<String> list = new LinkedList<>();
        for (int i = 0; i < 7; i++) {
            String s = sdf.format(calendar.getTime());
            list.addFirst(s);
            calendar.add(Calendar.DATE, -1);
        }
        return list;
    }
}
