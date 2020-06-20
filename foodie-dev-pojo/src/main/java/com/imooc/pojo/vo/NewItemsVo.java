package com.imooc.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class NewItemsVo {
    private String rootCatId;

    private String rootCatName;

    private String slogan;

    private String catImage;

    private String bgColor;

    List<SimpleItemVo> simpleItemList;
}
