package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

public interface CarouselService {
    /**
     * 查询所有轮播图列表
     * @param isShow
     */
     public List<Carousel> queryALl(Integer isShow);
}
