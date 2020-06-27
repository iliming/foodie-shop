package com.imooc.controller.center;

import com.imooc.pojo.Users;
import com.imooc.service.center.CenterUsersService;
import com.imooc.utils.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liming
 * @create 2020/6/27
 */
@Api(value = "门户相关",tags = "用门户相关的接口")
@RestController
@RequestMapping("center")
public class CenterController {
    @Resource
    private CenterUsersService centerUsersService;

    @ApiOperation(value = "查询用户信息",notes = "查询用户信息",httpMethod = "GET")
    @GetMapping("userInfo")
    public BaseResult userInfo(
            @ApiParam(value = "用户id",name = "userId",required = true)
            @RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            return BaseResult.errorMsg("用户id为空");
        }
        Users users = centerUsersService.queryUserInfo(userId);
        if(users == null){
            return BaseResult.errorMsg("用户不存在");
        }
        return BaseResult.ok(users);
    }
}
