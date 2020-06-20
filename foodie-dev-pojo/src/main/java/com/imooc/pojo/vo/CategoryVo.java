package com.imooc.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 二级分类VO
 */
@Data
public class CategoryVo {
    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;
    //三级分类VO
    private List<SubCategoryVo> subCatList;
}
