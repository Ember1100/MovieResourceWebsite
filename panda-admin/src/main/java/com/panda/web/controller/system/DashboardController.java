package com.panda.web.controller.system;


import com.panda.system.service.DashboardService;
import com.panda.web.controller.BaseController;
import com.panda.web.utils.ResultData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取总量
     */
    @GetMapping("/getTotal/{command}")
    public ResultData getTotalVisits(@PathVariable String command) throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Local HostAddress: "+addr.getHostAddress());
        String hostname = addr.getHostName();
        System.out.println("Local host name: "+hostname);
        Calendar calendar = Calendar.getInstance();
        Date endTime = calendar.getTime();
        if (command==null) {
            command = "";
        }
        switch (command) {
            case "近一周":
                calendar.add(Calendar.DATE, -7);
                break;
            case "近一个月":
                calendar.add(Calendar.MONTH, -1);
                break;
            case "近半年":
                calendar.add(Calendar.MONTH, -6);
                break;
            case "近一年":
                calendar.add(Calendar.YEAR, -1);
                break;
            default:
                break;
        }
        Date startTime = null;
        if (!calendar.getTime().toString().equals(endTime.toString())) {
            startTime = calendar.getTime();
        }
        return ResultData.ok(dashboardService.getTotal(startTime));
    }

    /**
     * 获取总量
     */
    @GetMapping("/getLineData/{type}")
    public ResultData getLineData(@PathVariable String type) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-7);
        return ResultData.ok(dashboardService.getLineDate(calendar.getTime(), type));
    }
}
