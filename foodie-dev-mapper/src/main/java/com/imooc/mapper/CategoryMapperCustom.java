package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVo;
import com.imooc.pojo.vo.NewItemsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapperCustom {
    public List<CategoryVo> getSubCatList(Integer rootCatId);

    List<NewItemsVo> getSixNewItemsLazy(@Param("rootCatId")String rootCatId);
}