package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.BaseResult;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Controller
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public BaseResult usernameIsExist(@RequestParam String username) {
        //1. 判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return BaseResult.errorMsg("用户名不能为空");
        }
        //2. 查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return BaseResult.errorMsg("用户名已经存在");
        }
        //3. 请求成功 用户名没有重复
        return BaseResult.ok();
    }

    @ApiOperation(value = "用户名注册", notes = "用户名注册", httpMethod = "POST")
    @PostMapping("/regist")
    public BaseResult regist(@RequestBody UserBO userBO,HttpServletRequest request,HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        //1.判断用户名密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) || StringUtils.isBlank(confirmPwd)) {
            return BaseResult.errorMsg("用户名或密码不能为空");
        }
        //2.查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return BaseResult.errorMsg("用户名已经存在");
        }
        //3.密码长度不能小于6位
        if (password.length() < 6) {
            return BaseResult.errorMsg("密码长度不能小于6");
        }
        //4.判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return BaseResult.errorMsg("两次密码输入不一致");
        }
        //5/实现注册
        Users userResult = userService.createUser(userBO);
        //把用户信息放到Coikie里（前后端都可以做）展示登录成功显示的信息，true 表示传给前端的cookie需不需要加密
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);
        return BaseResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public BaseResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        //1.判断用户名密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return BaseResult.errorMsg("用户名或密码不能为空");
        }
        //2.实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null) {
            return BaseResult.errorMsg("用户名或密码不正确");
        }
        userResult = setNullProperty(userResult);
        //把用户信息放到Coikie里（前后端都可以做）展示登录成功显示的信息，true 表示传给前端的cookie需不需要加密
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);
        return BaseResult.ok(userResult);
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setRealname(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public BaseResult logout(@RequestBody String userId,HttpServletRequest request, HttpServletResponse response){
        //用户退出以后相应的cookie信息需要被清除
        CookieUtils.deleteCookie(request,response,"user");
        //TODO 用户退出登录，需要清空购物车
        //TODO 分布式会话中需要清除用户数据
        return BaseResult.ok();
    }

}
