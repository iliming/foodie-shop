package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class SearchItemsVo {
    private String itemName;

    private String itemId;

    private int sellCounts;

    private String imgUrl;

    private int price;
}
