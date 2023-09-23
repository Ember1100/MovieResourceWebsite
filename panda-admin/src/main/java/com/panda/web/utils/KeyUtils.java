package com.panda.web.utils;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.stereotype.Service;

import java.util.Random;

@SpringBootConfiguration
@Service
public class KeyUtils {

    /**
     * 产生独一无二的key
     *
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
