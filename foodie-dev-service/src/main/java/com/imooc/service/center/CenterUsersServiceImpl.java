package com.imooc.service.center;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.CenterUserBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liming
 * @create 2020/6/27
 */
@Service
public class CenterUsersServiceImpl implements CenterUsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users user = new Users();
        BeanUtils.copyProperties(centerUserBO,user);
        user.setId(userId);
        user.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserInfo(userId);
    }

    @Override
    public Users updateUserFace(String userId, String finalUserFaceUrl) {
        Users user = new Users();
        user.setId(userId);
        user.setUpdatedTime(new Date());
        user.setFace(finalUserFaceUrl);
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserInfo(userId);
    }
}
