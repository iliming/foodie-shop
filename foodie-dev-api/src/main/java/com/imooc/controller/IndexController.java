package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVo;
import com.imooc.pojo.vo.NewItemsVo;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public BaseResult carousel(){
        List<Carousel> list = carouselService.queryALl(YesOrNo.YES.type);
        return BaseResult.ok(list);
    }


    @ApiOperation(value = "获取商品分类（一级分类）", notes = "获取商品分类（一级分类）", httpMethod = "GET")
    @GetMapping("/cats")
    public BaseResult cats(){
        List<Category> list = categoryService.queryALlRootCat();
        return BaseResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public BaseResult subCats(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId==null){
            return BaseResult.errorMsg("分类不存在");
        }
        List<CategoryVo> list = categoryService.grtSubCatList(rootCatId);
        return BaseResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下最新的6条数据",notes = "查询每个一级分类下最新的6条数据",httpMethod = "GET")
    @GetMapping("sixNewItems/{rootCatId}")
    public BaseResult cats(@ApiParam(name = "rootCatId",value = "一级分类id",required = true)
                                   @PathVariable("rootCatId") String rootCatId){
        if(StringUtils.isBlank(rootCatId)){
            return BaseResult.errorMsg("该分类下商品不存在");
        }
        List<NewItemsVo> newItemsVOS = categoryService.getSixNewItemsLazy(rootCatId);
        return BaseResult.ok(newItemsVOS);
    }


}
