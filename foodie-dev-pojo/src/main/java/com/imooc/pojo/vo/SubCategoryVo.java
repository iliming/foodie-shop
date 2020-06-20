package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class SubCategoryVo {
    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;
}
