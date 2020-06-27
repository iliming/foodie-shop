package com.imooc.exception;

import com.imooc.utils.BaseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author liming
 * @create 2020/6/27
 */
//自定义的一个异常捕获助手类 使用下面注解
@RestControllerAdvice
public class CustomExceptionHandler {
    //对MaxUploadSizeExceededException 捕捉全局异常 对所有requestMapping有效
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResult handlerMaxUploadException(MaxUploadSizeExceededException e){
        return BaseResult.errorMsg("文件大小超过限制200kb，请压缩图片在上传");
    }
}
