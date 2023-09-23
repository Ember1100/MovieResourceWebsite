package com.panda.web.controller.system;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.panda.system.domin.param.PayOrderParam;
import com.panda.system.service.MemberOrderService;
import com.panda.web.utils.QrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ThirdGoddess
 * @version 1.0.0
 * @time 2022/12/22 14:37
 * @desc AliPay当面付Demo
 */
@RestController
@RequestMapping("pay")
public class AliPayController {



    //==================================================================================================================
    //这里都是固定的

    //支付宝网关地址
    private static final String SERVER_URL = "https://openapi.alipaydev.com/gateway.do";

    //charset
    private static final String CHARSET = "UTF-8";

    //format
    private static final String FORMAT = "json";

    //sign type
    private static final String SIGN_TYPE = "RSA2";

    //==================================================================================================================
    //下面这三个是需要配置的

    //APPID，即创建应用的那个ID,在应用详情中的左上角可以看到
    private static final String APPID = "2021000122632670";

    //应用私钥，注意是应用私钥！！！应用私钥！！！应用私钥！！！
    private static final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDFFQRR/qq6qf9DkJBuZH5hRwF4+j+yuvxTCJ7W5XNnp286G31Wx4Pi5/pufKUquNRCdKv63FqTXQ0GwWmz33PUGRVSKRrX5CNYTsiJDHZ4CFLZglxWfKGKc3Jx4JudVuuJHsVSkD17VvBYfBc7PIID1TrRyk9BKXeC953r59PAuGcW3zuyjkx2oeBqst2Q+7TwYSRFUMzfeiqTv5AhDI5JSbl+Ef76MbuLaxJnwPvxotLEBTUIr0Gn3dwH4OlKJn9mRsGA3M1vXxWdEQ5pwPczWrXr/AvgFvcEgiReBelpec0tq1fGCe5S9zyYNeFtIj/V0WAAUelNzPj5222LlH/FAgMBAAECggEAAK7A9BPAvaLHGpHOnkNE2MtpKuUbAWB6bUMzYaUr0CkZToc32OwZ/dg12todrFc1jQFgcx7+RVJmMCbNWe0lNo/brgwsZqEIqk6jjS343xOQTeaQsU//Mz6RH+naQCJD8JalslKDlADKFlRftDnSbXb4nC9qNNyS6GY5Ks8DwcAgNIRLEvIfApdUOm53HD9Qhi+9Bm7DNhIb+g0/+JjlHK+8ei57mp37PTTuRokppi8J3VFYH/BBAkUdOQ5ieSgucOZnxEB5WqOfMsWTCy5K5YzXswJaKHLf1C46JbPxUZY6hSm//Rqd1KHpIEApYPWbvnl2oN53pRQCO/Z7ISFUoQKBgQDrN8X4XFObaECEnqHVIEhGvuqcHeXc/q/5/GzA/DCwWm2TbkJrigsjfB1Uxas0vX3Z/MOe2unXm+c70paYAmmkGERD59rVTk76DXOPmDCqcdTAJOfHXn1V6VFwNqKf2SZbJsEreYcMxGAexmtl5wuUCISfrvL8SXmkHW8dYjqJCQKBgQDWfq110O8uj7CBSHcMJco+QzYC4t094Y5dhQYYns6pZpKo+Uz/lvZDRB3UyjCiuZJPNqsYrehvGDrc6ixmGMLy5U1MXPci2ytbZiUIrjlgGujry4g81iUoX+uKgYG2W9UEMXb4lkKsRZQBcZuI65nTLg17Jmh/AnXwhehDESZb3QKBgGaC7iKpij6Glue1gu4xnY5HklPrq8w+q08Vtgf9YPLa1jqUqRnZFbWnV9kkNNNNalXglRjca2FuLr6hrsQc+RFtCgNWPscL09uilSSVvU/rQD9lViZLhfA1OSrghMuJhe/f2fwqgLMNup5u90t4AFHJzJMTWg6drwfZVB3IO56RAoGBAIJB5acRnd7BxeeNETagUGRlPoycJs0oh+NFIEGdV1VKhC/3TYo9rAmHeTI7IzP+y9otEdRn8CpfJYf48cOCsAj/tWR1ZGOXg4n6yuytmQxC60YekPqY9XHCZnDzWslalU75lw05ev3RNyN1UYXhxcWYRvmNhpHy9Zp+R2FLLedFAoGAcgbg/fxMm+lLee4+ZebB8+Mol6UB0SrZjOzYES4tsB91esVBceEyXIGKiirn/DXUpjpl99f5niTFuU1sZcSfEue03bjs0c9u5v0oa8koJUHEMqVDLufnLbeC32B8VAIUtFW1/2DtgvkAn5Fk7sRyDL/kHiCWIG5dBpamsK07foM=";

    //支付宝公钥，注意是支付宝公钥！！！支付宝公钥！！！支付宝公钥！！！
    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjnaA9D1nRK5q1QtE4a8JPA2kBDMoajq5Oh8cyMl18JrrAyRGe740+KtOiSFCrxsfpw5Kn2v4mpfSRHa+LPD8uwPSvGCTbzxvKlAosm7AImuBn0TF6WtQc7nwuq6SxoTef0Iin7t/MTP/ySahGJyaVXW24hqzxxJ32ltYqkz1inWY4m5POIJwE6jJkf/HcnsU4X2+CwgdTrJO7G2cPhDsjw9DaZ6cv7BKuCKmGwL3G3gWqdlQS4TmjCmavWhG4gsuVViGGfNcY28TvkmmH/rhuUT5FBUfjz+9529MWQvXCaIjMOu1lj/XSh2BNWo2RLDat8i6XLdFlM/hTtFz0A8I/QIDAQAB";
    @Autowired
    private MemberOrderService memberOrderService;
    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取二维码
     * 获取的是用户要扫码支付的二维码
     * 创建订单，带入自己的业务逻辑
     */
    @RequestMapping(value = "/getQr", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getQr(PayOrderParam payOrder) throws UnsupportedEncodingException {

        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APPID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        //配置这是一个url，下图我已经配置好了，这个意思是当用户成功后，支付宝那边会调用这个地址url，他会给你传过去一些订单信息，
        //你处理完你的业务逻辑给支付宝响应success就行，就代表这个订单完成交易了！
        //* 建议前期开发的时候加上内网穿透调试，不然支付宝是没有办法调到你开发的接口的
        request.setNotifyUrl("http://www.moreluck.cc:8000/pay/payNotification");

        JSONObject bizContent = new JSONObject();

        //自己生成一个订单号，我这里直接用时间戳演示，正常情况下创建完订单需要存储到自己的业务数据库，做记录和支付完成后校验
        String orderNumber = "pay" + payOrder.getSign();
        payOrder.setOrderNumber(orderNumber);
        bizContent.put("out_trade_no", orderNumber);//订单号
        bizContent.put("total_amount", payOrder.getPrice());//订单金额
        bizContent.put("subject",payOrder.getProductName());//支付主题，自己稍微定义一下
        request.setBizContent(bizContent.toString());
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }

            //获取生成的二维码，这里是一个String字符串，即二维码的内容；
            //然后用二维码生成SDK生成一下二维码，弄成图片返回给前端就行,我这里使用Zxing生成
            //其实也可以直接把这个字符串信息返回，让前端去生成，一样的道理，只需要关心这个二维码的内容就行
            String qrCode = response.getQrCode();
            //生成支付二维码图片
            BufferedImage image = QrCodeUtil.createImage(qrCode);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", out);

            byte[] b = out.toByteArray();
            out.write(b);
            out.close();
            payOrder.setQrCode(qrCode);
            redisTemplate.opsForValue().set(orderNumber, payOrder, 600, TimeUnit.SECONDS);
            //最终返回图片
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用失败");
        }
        return null;
    }

    /**
     * 支付完成后支付宝会请求这个回调
     */
    @PostMapping("payNotification")
    public String payNotification(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }


        //==============================================================================================================
        try {
            //执行验签，确保结果是支付宝回调的，而不是被恶意调用，一定要做这一步
            boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE);
            if (signVerified) {
                //验签成功，继续执行业务逻辑
                System.out.println("验签成功");

                //再次主动查询订单，不要只依赖支付宝回调的结果
                String orderStatus = searchOrderStatus(params.get("out_trade_no"), params.get("trade_no"));
                switch (orderStatus) {
                    case "TRADE_SUCCESS"://交易支付成功；
                        PayOrderParam pay = (PayOrderParam) redisTemplate.opsForValue().get(params.get("out_trade_no"));
                        pay.setStatus("支付成功");
                        redisTemplate.delete(params.get("out_trade_no"));
                        redisTemplate.opsForValue().set(params.get("out_trade_no"), pay,600, TimeUnit.SECONDS);
                        memberOrderService.addVipServiceRecord(pay);
                    case "TRADE_FINISHED": //交易结束，不可退款；
                        //TODO 在这里继续执行用户支付成功后的业务逻辑
//                        userPayState = true;
                        break;
                }
                return "success";
            } else {
                //验签失败（很可能接口被非法调用）
                System.out.println("验签失败");
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 封装一个订单查询
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号。支付宝交易凭证号
     * @return 订单状态：String
     * @throws AlipayApiException AlipayApiException
     * @desc "WAIT_BUYER_PAY":交易创建，等待买家付款；"TRADE_CLOSED":未付款交易超时关闭，或支付完成后全额退款； "TRADE_SUCCESS":交易支付成功；"TRADE_FINISHED":交易结束，不可退款；
     */
    private String searchOrderStatus(String outTradeNo, String tradeNo) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APPID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
        AlipayTradeQueryRequest aliRequest = new AlipayTradeQueryRequest();//创建API对应的request类
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("trade_no", tradeNo);
        aliRequest.setBizContent(bizContent.toString()); //设置业务参数
        AlipayTradeQueryResponse response = alipayClient.execute(aliRequest);//通过alipayClient调用API，获得对应的response类
        JSONObject responseObject = JSONObject.parseObject(response.getBody());
        JSONObject alipayTradeQueryResponse = responseObject.getJSONObject("alipay_trade_query_response");
        return alipayTradeQueryResponse.getString("trade_status");
    }

    /**
     * 前端轮询查询这个接口，来查询订单的支付状态
     *
     * @return OrderStateEntity
     */
    @CrossOrigin
    @GetMapping("searchOrder/{sign}")
    public OrderStateEntity searchOrder(@PathVariable String sign) {
        System.out.println(sign);
        PayOrderParam pay = (PayOrderParam) redisTemplate.opsForValue().get("pay" + sign);
        System.out.println(pay);
        //userPayState是一个模拟值
        if (pay != null && pay.getStatus() != null && pay.getStatus().equals("支付成功")) {
            //用户支付成功了
            return new OrderStateEntity(200, "支付成功了");
        } else {
            //用户还没有支付
            return new OrderStateEntity(201, "你还没有支付哦");
        }
    }

    /**
     * 响应给前端的实体
     */
    static class OrderStateEntity {
        private int code;
        private String msg;

        public OrderStateEntity(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}

