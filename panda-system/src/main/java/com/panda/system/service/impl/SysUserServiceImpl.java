package com.panda.system.service.impl;

import com.panda.common.utils.JwtUtil;
import com.panda.common.utils.SaltUtils;
import com.panda.system.domin.LoginUser;
import com.panda.system.domin.SysUser;
import com.panda.system.domin.UserLoginLog;
import com.panda.system.domin.UserService;
import com.panda.system.domin.vo.SysUserVo;
import com.panda.system.mapper.SysUserMapper;
import com.panda.system.mapper.UserLoginLogMapper;
import com.panda.system.mapper.UserServiceMapper;
import com.panda.system.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    @Autowired
    private UserServiceMapper userServiceMapper;

    @Override
    public List<SysUser> findAllUsers(SysUser sysUser) {
        return sysUserMapper.findAllUsers(sysUser);
    }

    @Override
    public SysUser findUserById(Long id) {
        return sysUserMapper.findUserById(id);
    }

    @Override
    public SysUser findUserByName(String userName) {
        return sysUserMapper.findUserByName(userName);
    }

    /**
     * 处理注册逻辑
     *
     * @param sysUser
     * @return
     */
    @Override
    public int addUser(SysUser sysUser) {

        if (!isUserNameUnique(sysUser.getUserName(), -1L)) {
            throw new AuthenticationException("用户名重复");
        }
        //处理密码：md5 + salt + hash散列
//        salt，获得8位的随机盐
        String salt = SaltUtils.getSalt(8);
//        md5加密（sysUser.getPassword(), salt）
        Md5Hash md5Hash = new Md5Hash(sysUser.getPassword(), salt, 1024);

        sysUser.setPassword(md5Hash.toHex());
        sysUser.setSalt(salt);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date());
        sysUser.setRegisterTime(s);
        return sysUserMapper.addUser(sysUser);
    }

    @Override
    public int updateUser(SysUser sysUser) {
        if (!isUserNameUnique(sysUser.getUserName(), sysUser.getUserId())) {
            throw new AuthenticationException("用户名重复");
        }
        SysUser originUser = sysUserMapper.findUserById(sysUser.getUserId());
        if (originUser == null) {
            throw new AuthenticationException("用户不存在");
        }

        if (!originUser.getPassword().equals(sysUser.getPassword())) {
            //修改了密码
            //重新处理密码存储
            String salt = SaltUtils.getSalt(8);
            Md5Hash md5Hash = new Md5Hash(sysUser.getPassword(), salt, 1024);

            sysUser.setPassword(md5Hash.toHex());
            sysUser.setSalt(salt);
        }
        return sysUserMapper.updateUser(sysUser);
    }

    @Override
    public int deleteUser(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysUserMapper.deleteUser(id);
            userServiceMapper.deleteByPrimaryKey(id);
        }
        return rows;
    }

    @Override
    public int deleteUser(Long id) {
        sysUserMapper.deleteUser(id);
        UserService userService = new UserService();
        userService.setUserId(id);
        userService.setStatus(-1);
        userServiceMapper.updateByUserId(userService);
        return 0;
    }

    @Override
    public LoginUser login(SysUserVo sysUserVo) {
        Date date = new Date();
        //登录，先查询用户信息
        SysUser user = sysUserMapper.findUserByName(sysUserVo.getUserName());
        if (user == null) {
            throw new AuthenticationException("用户名不存在");
        }

        //验证密码(拿到的password+数据库的salt)
        Md5Hash md5Hash = new Md5Hash(sysUserVo.getPassword(), user.getSalt(), 1024);
        if (!user.getPassword().equals(md5Hash.toHex())) {
            throw new AuthenticationException("用户名或密码错误");
        }

        //设置登录用户对象
        LoginUser loginUser = findLoginUser(sysUserVo);

        //颁发token
        String token = JwtUtil.sign(user.getUserName(), user.getPassword());

        loginUser.setToken(token);


        //保存用户登录记录
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setUserId(user.getUserId());
        userLoginLog.setLoginTime(date);
        userLoginLogMapper.insertSelective(userLoginLog);

        return loginUser;
    }


    @Override
    public LoginUser findLoginUser(SysUserVo sysUserVo) {
        return sysUserMapper.findLoginUser(sysUserVo);
    }

    //返回用户状态

    @Override
    public boolean isUserNameUnique(String userName, Long userId) {
        List<Long> userIds = sysUserMapper.checkUserNameUnique(userName);
        for (Long id : userIds) {
            if (id.equals(userId)) {
                return true;
            }
        }
        return userIds.isEmpty();
    }



    @Override
    public Boolean getServerStatus(Long userId) {
        //查看用户是否是会员身份
        UserService userService = userServiceMapper.selectByUserId(userId);
        //未开通会员服务
        if (null==userService) {
            return Boolean.FALSE;
        }else {
            //判断服务是否过期
            if (userService.getDayRemain() < 0) {
                //服务过期
                //服务过期
                UserService updateUser = new UserService();
                updateUser.setUserId(userService.getUserId());
                updateUser.setStatus(-1);
                userServiceMapper.updateByPrimaryKeySelective(updateUser);
                return Boolean.FALSE;
            } else if (userService.getDayRemain() == 0) {
                //会员过期当天
                return Boolean.TRUE;
            } else {
                return Boolean.TRUE;
            }
        }
    }
}
