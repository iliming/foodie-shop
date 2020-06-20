package com.imooc.controller;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车接口controller", tags = {"购物车接口controller"})
@RequestMapping("shopcart")
@RestController
public class ShopCatController {
    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public BaseResult add(@RequestParam String userId, @RequestBody ShopcartBO shopcartBO,
                      HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return BaseResult.errorMsg("");
        }
        // TODO 前端用户在登录的情况下，会同步时购物车到redis缓存
        System.out.println(shopcartBO);

        return BaseResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public BaseResult del(@RequestParam String userId, @RequestParam String itemSpecId,
                      HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId) ) {
            return BaseResult.errorMsg("参数不能为空");
        }
        // TODO 页面删除购物车中的数据，会同步时购物车到redis缓存

        return BaseResult.ok();
    }
}
