package com.imooc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户对象BO",description = "从客户端，由用户传递的数据封装在此entity")
public class UserBO {
    @ApiModelProperty(value = "用户名",name = "username",example = "imooc",required = true)
    private String username;
    @ApiModelProperty(value = "密码",name = "password",example = "123456",required = true)
    private String password;
    @ApiModelProperty(value = "确认密码",name = "confirmPassword",example = "123456",required = false)//确认密码不是必填项
    private String confirmPassword;
}
