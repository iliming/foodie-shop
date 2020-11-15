package com.imooc.mapper;

import com.imooc.pojo.ItemBaseInfo;

/**
 * Created by liming on 2020/11/15 3:24 下午
 */
public interface ItemBaseInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ItemBaseInfo record);

    int insertSelective(ItemBaseInfo record);

    ItemBaseInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ItemBaseInfo record);

    int updateByPrimaryKey(ItemBaseInfo record);
}