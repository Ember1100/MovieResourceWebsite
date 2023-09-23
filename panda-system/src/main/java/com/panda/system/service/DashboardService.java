package com.panda.system.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    List<Object> getTotal(Date startTime);

    Map getLineDate(Date startTime, String type) throws ParseException;
}
