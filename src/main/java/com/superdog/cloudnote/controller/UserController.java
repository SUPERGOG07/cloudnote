package com.superdog.cloudnote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.superdog.cloudnote.common.JwtUtil;
import com.superdog.cloudnote.common.R;
import com.superdog.cloudnote.pojo.User;
import com.superdog.cloudnote.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation("注册")
    public R<String> register(@RequestBody User user){
        log.info("注册请求-->{}",user.toString());

        //检测用户单一性
        if(userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName,user.getUserName()))!=null){
            log.info("注册失败-->{}",user.toString());
            return R.error("用户已经存在");
        }

        //密码加密和默认昵称
        String pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(pwd);
        user.setNickName(user.getUserName());

        //加入数据库
        userService.save(user);

        return R.success("注册成功");
    }

    @PostMapping("/login")
    @ApiOperation("登陆")
    public R<Map<String,Object>> login(@RequestBody User user){
        log.info("登陆请求-->{}",user.getUserName());

        //用户检测
        User resultUser = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName,user.getUserName()));
        if(resultUser == null){
            log.info("登陆失败-->{}",user.getUserName());
            return R.error("登陆失败，不存在该用户");
        }
        if (!resultUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))){
            log.info("登陆失败-->{}",user.getUserName());
            return R.error("密码错误");
        }

        log.info("登陆成功-->{}",user.getUserName());

        //设置载荷声明
        Map<String,String> claim = new HashMap<>();
        claim.put("userName", resultUser.getUserName());

        //设置结果集
        Map<String,Object> result = new HashMap<>();
        result.put("user",resultUser);
        result.put("token", JwtUtil.createToken(claim));

        return R.success(result);
    }
}
