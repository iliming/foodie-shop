package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.CenterUserBO;

/**
 * @author liming
 * @create 2020/6/27
 */
public interface CenterUsersService {
    Users queryUserInfo(String userId);

    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    Users updateUserFace(String userId, String finalUserFaceUrl);
}
