package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUsersService;
import com.imooc.utils.BaseResult;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liming
 * @create 2020/6/27
 */
@Api(value = "用户信息相关的api接口",tags = "用户信息相关的api接口")
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private CenterUsersService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "更新用户信息",notes = "更新用户信息",httpMethod = "POST")
    @PostMapping("update")
    public BaseResult update(
            @ApiParam(value = "用户id",name = "userId",required = true)
            @RequestParam String userId,
            @Valid @RequestBody CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response){
        if(result.hasErrors()){
            Map<String,String> map = getErrors(result);
            return BaseResult.errorMap(map);
        }
        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        //TODO 后续会改，增加令牌token 整合redis
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        return BaseResult.ok();
    }



    @ApiOperation(value = "更新用户头像",notes = "更新用户头像",httpMethod = "POST")
    @PostMapping("uploadFace")
    public BaseResult uploadFace(
            @ApiParam(value = "用户id",name = "userId",required = true)
            @RequestParam String userId,
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response){

        // 定义头像保存地址
        // String fileSpace = IMAGE_USER_FACE_LOCATION;
        //改为用配置文件设置地址
        String fileSpace = fileUpload.getImageUserFaceLocation();
        // 在路径上为每一个用户增加一个userid，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        // 开始文件上传
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                // 获得文件上传的文件名称
                String fileName = file.getOriginalFilename();
                // 对头像文件名处理成统一格式 face-{userId}.png
                if (StringUtils.isNotBlank(fileName)) {
                    String fileNameArr[] = fileName.split("\\.");

                    // 获取文件的后缀名
                    String suffix = fileNameArr[fileNameArr.length - 1];
                    if (!suffix.equalsIgnoreCase("png") &&
                            !suffix.equalsIgnoreCase("jpg") &&
                            !suffix.equalsIgnoreCase("jpeg") ) {
                        return BaseResult.errorMsg("图片格式不正确！");
                    }
                    // 文件名称重组 这种是覆盖式上传，想要增量式的不覆盖：额外拼接当前时间
                    String newFileName = "face-" + userId + "." + suffix;
                    // 上传的头像最终保存的位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    // 用于提供给web服务访问的地址
                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File(finalFacePath);
                    // 上传路径没有 则创建文件夹
                    if (outFile.getParentFile() != null) {
                        // 创建文件夹，mkdirs会递归生成多级
                        outFile.getParentFile().mkdirs();
                    }
                    // 文件输出保存到目录 ，把从前端获得的输入流，转换成输出流 输出到指定路径
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return BaseResult.errorMsg("文件不能为空！");
        }
        // 获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();
        // 由于浏览器可能存在缓存的情况，所以在这里，需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return BaseResult.ok();
    }



    private Map<String,String> getErrors(BindingResult result){
        Map<String,String> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for(FieldError fieldError : fieldErrors){
            String field = fieldError.getField();
            String errorMsg = fieldError.getDefaultMessage();
            errorMap.put(field,errorMsg);
        }
        return errorMap;
    }

}
