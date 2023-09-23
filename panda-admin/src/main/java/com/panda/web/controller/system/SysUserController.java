package com.panda.web.controller.system;

import com.panda.common.response.ResponseResult;
import com.panda.system.domin.SysUser;
import com.panda.system.domin.vo.SysUserVo;
import com.panda.system.service.impl.SysUserServiceImpl;
import com.panda.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysUserController extends BaseController {
    @Autowired
    private SysUserServiceImpl sysUserService;

    @GetMapping("/sysUser")
    public ResponseResult findAllUsers(SysUser sysUser) {
        startPage();
        List<SysUser> data = sysUserService.findAllUsers(sysUser);
        return getResult(data);
    }

    /**
     * 用户注销
     * @param id
     * @return
     */
    @PostMapping("/sysUser/delUser/{id}")
    public ResponseResult delUser(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return new ResponseResult(200,"操作成功");
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/sysUser/{id}")
    public ResponseResult findUserById(@PathVariable Long id) {
        return getResult(sysUserService.findUserById(id));
    }

    /**
     * 添加用户请求，注册也在这里
     * @param sysUser
     * @return
     */
    @PostMapping("/sysUser")
    public ResponseResult addUser(@Validated @RequestBody SysUser sysUser) {
        return getResult(sysUserService.addUser(sysUser));
    }

    /**
     *更新
     * @param sysUser
     * @return
     */
    @PutMapping("/sysUser")
    public ResponseResult updateUser(@Validated @RequestBody SysUser sysUser) {
        return getResult(sysUserService.updateUser(sysUser));
    }

    /**
     *删除
     * @param ids
     * @return
     */
    @DeleteMapping("/sysUser/{ids}")
    public ResponseResult deleteUser(@PathVariable Long[] ids) {
        return getResult(sysUserService.deleteUser(ids));
    }

    /**
     * 用户登录请求
     *
     * @param sysUserVo 封装用户登录输入的信息
     * @return
     */
    @RequestMapping("/sysUser/login")
    public ResponseResult login(@RequestBody SysUserVo sysUserVo) {

        return getResult(sysUserService.login(sysUserVo));
    }

    /**
     * 获取用户服务信息
     */
    @RequestMapping("/sysUser/getServerStatus/{id}")
    public ResponseResult getServerStatus(@PathVariable Long id) {
        Boolean serverStatus = sysUserService.getServerStatus(id);
        return new ResponseResult(200,"success",serverStatus);
    }

    /**
     * 用户注册请求
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/sysUser/register")
    public ResponseResult register(@Validated @RequestBody SysUser sysUser) {
        // 注册只接收部分参数值，重新建立一个实例对象只接受注册接受的参数
        SysUser registerUserInfo = new SysUser();
        registerUserInfo.setUserName(sysUser.getUserName());
        registerUserInfo.setPassword(sysUser.getPassword());
        registerUserInfo.setSex(sysUser.getSex());
        registerUserInfo.setPhoneNumber(sysUser.getPhoneNumber());
        registerUserInfo.setUserPicture("[\"http://1.12.42.181:9001/images/33.png\"]");
        return getResult(sysUserService.addUser(registerUserInfo));
    }


}
