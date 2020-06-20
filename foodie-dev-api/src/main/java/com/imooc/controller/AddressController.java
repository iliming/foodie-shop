package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import com.imooc.utils.BaseResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关",tags={"地址相关的api接口"})
@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 1.查询用户相对应的地址列表
     * 2.新增地址
     * 3.修改地址
     * 4.删除地址
     * 5.设置默认地址
     */
    @ApiOperation(value = "展示地址列表",notes = "展示地址列表",httpMethod = "GET")
    @PostMapping("list")
    public BaseResult list(@RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            return BaseResult.errorMsg("用户id参数没传入");
        }
        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return BaseResult.ok(userAddresses);
    }

    @ApiOperation(value = "用户新增地址",notes = "用户新增地址",httpMethod = "POST")
    @PostMapping("add")
    public BaseResult add(@RequestBody AddressBO addressBO){
        BaseResult result = checkAddress(addressBO);
        if(result.getStatus() != 200){
            return result;
        }
        addressService.addNewUserAddress(addressBO);
        return BaseResult.ok();
    }

    @ApiOperation(value = "用户修改地址",notes = "用户修改地址",httpMethod = "POST")
    @PostMapping("update")
    public BaseResult update(@RequestBody AddressBO addressBO){
        if(StringUtils.isBlank(addressBO.getAddressId())){
            BaseResult.errorMsg("修改地址错误AddressId为空");
        }
        BaseResult result = checkAddress(addressBO);
        if(result.getStatus() != 200){
            return result;
        }
        addressService.updateUserAddress(addressBO);
        return BaseResult.ok();
    }

    @ApiOperation(value = "用户删除地址",notes = "用户删除地址",httpMethod = "POST")
    @PostMapping("delete")
    public BaseResult delete(@RequestParam String userId,
                                     @RequestParam String addressId){
        if(StringUtils.isBlank(userId)){
            BaseResult.errorMsg("用户id为空");
        }
        if(StringUtils.isBlank(addressId)){
            BaseResult.errorMsg("用户地址id为空");
        }
        addressService.deleteUserAddress(userId,addressId);
        return BaseResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址",notes = "用户设置默认地址",httpMethod = "POST")
    @PostMapping("setDefalut")
    public BaseResult setDefalut(@RequestParam String userId,
                                         @RequestParam String addressId){
        if(StringUtils.isBlank(userId)){
            BaseResult.errorMsg("用户id为空");
        }
        if(StringUtils.isBlank(addressId)){
            BaseResult.errorMsg("用户地址id为空");
        }
        addressService.setDefalutAddress(userId,addressId);
        return BaseResult.ok();
    }

    private BaseResult checkAddress(AddressBO addressBO){
        String receiver = addressBO.getReceiver();
        if(StringUtils.isBlank(receiver)){
            return BaseResult.errorMsg("收货人不能为空");
        }
        if(receiver.length()> 12){
            return BaseResult.errorMsg("收货人姓名不能太长");
        }
        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return BaseResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return BaseResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return BaseResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return BaseResult.errorMsg("收货地址信息不能为空");
        }

        return BaseResult.ok();
    }
}
