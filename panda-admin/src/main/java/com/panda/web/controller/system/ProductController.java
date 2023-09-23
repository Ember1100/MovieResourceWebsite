package com.panda.web.controller.system;

import com.panda.system.domin.Product;
import com.panda.system.domin.SysUser;
import com.panda.system.domin.param.OrderParam;
import com.panda.system.domin.param.PayOrderParam;
import com.panda.system.domin.vo.SysMovieVo;
import com.panda.system.domin.vo.UserServiceDetailVo;
import com.panda.system.service.MemberOrderService;
import com.panda.system.service.ProductService;
import com.panda.system.service.SysUserService;
import com.panda.web.controller.BaseController;
import com.panda.web.utils.MinioUtil;
import com.panda.web.utils.ResultData;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private MemberOrderService memberOrderService;


    /**
     * 获取会员产品列表
     * @return
     */
    @GetMapping("/getList")
    public ResultData getList() {
        List<Product> list = productService.getList();
        return ResultData.ok(list);
    }

    @PostMapping("/buyProduct")
    public ResultData buyProduct(@RequestBody PayOrderParam param){
        if (param.getUserId()==null) {
            return new  ResultData(200,"请先登录");
        }
        memberOrderService.addVipServiceRecord(param);
        return new ResultData(200,"支付成功");
    }

    /**
     * 获取用户订单
     * @param param
     * @return
     */
    @GetMapping("/getUserMemberOrder")
    public ResultData getUserMemberOrder(OrderParam param) {
        if (param.getUserId() == null) {
            return new ResultData(200,"userId为null");
        }
        return ResultData.ok(memberOrderService.getUserMemberOrder(param));
    }

    /**
     * 获取用户会员详细信息
     * @param userId
     * @return
     */
    @GetMapping("/getUserServiceDetail/{userId}")
    public ResultData getUserServiceDetail(@PathVariable Long userId) {
        if (userId == null) {
            return new ResultData(200,"userId为null");
        }
        ArrayList<UserServiceDetailVo> list = new ArrayList<>();
        list.add(memberOrderService.getUserServiceDetail(userId));
        return ResultData.ok(list);
    }

}
