package com.imooc.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.imooc.pojo.ItemBaseInfo;
import com.imooc.mapper.ItemBaseInfoMapper;
/**
 * Created by liming on 2020/11/15 3:24 下午
 */
@Service
public class ItemBaseInfoService{

    @Resource
    private ItemBaseInfoMapper itemBaseInfoMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return itemBaseInfoMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(ItemBaseInfo record) {
        return itemBaseInfoMapper.insert(record);
    }

    
    public int insertSelective(ItemBaseInfo record) {
        return itemBaseInfoMapper.insertSelective(record);
    }

    
    public ItemBaseInfo selectByPrimaryKey(Long id) {
        return itemBaseInfoMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(ItemBaseInfo record) {
        return itemBaseInfoMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(ItemBaseInfo record) {
        return itemBaseInfoMapper.updateByPrimaryKey(record);
    }

}
