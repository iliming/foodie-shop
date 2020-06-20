package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVo;
import com.imooc.pojo.vo.NewItemsVo;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有一级分类
     * @param
     */
     public List<Category> queryALlRootCat();

    /**
     * 根据一级分类id查询子分类信息
     */
    public List<CategoryVo> grtSubCatList(Integer rootCatId);

    /**
     * 查询每个一级分类下最新的6条数据，不是主图就以时间排序
     * @param rootCatId
     * @return
     */
    List<NewItemsVo> getSixNewItemsLazy(String rootCatId);
}
