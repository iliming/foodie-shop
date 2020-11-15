package com.imooc.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liming on 2020/11/15 3:24 下午
 */
/**
    * 商品主表
    */
@ApiModel(value="com-imooc-pojo-ItemBaseInfo")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemBaseInfo implements Serializable {
    /**
    * 商品id
    */
    @ApiModelProperty(value="商品id")
    private Long id;

    /**
    * 供应商userId
    */
    @ApiModelProperty(value="供应商userId")
    private Long vendorUserId;

    /**
    * 类目ID
    */
    @ApiModelProperty(value="类目ID")
    private Short categoryId;

    /**
    * 商品编码
    */
    @ApiModelProperty(value="商品编码")
    private String itemCode;

    /**
    * 商品标题
    */
    @ApiModelProperty(value="商品标题")
    private String title;

    /**
    * 商品标语
    */
    @ApiModelProperty(value="商品标语")
    private String slogan;

    /**
    * sku最低价
    */
    @ApiModelProperty(value="sku最低价")
    private Long price;

    /**
    * 商品状态（0-删除，1-被驳回， 2-待审核， 3-待售， 4-上架/在售）
    */
    @ApiModelProperty(value="商品状态（0-删除，1-被驳回， 2-待审核， 3-待售， 4-上架/在售）")
    private Boolean status;

    /**
    * 销售类型（1-普通）
    */
    @ApiModelProperty(value="销售类型（1-普通）")
    private Byte saleType;

    /**
    * 商品轮播图首图
    */
    @ApiModelProperty(value="商品轮播图首图")
    private String cover;

    /**
    * 商品详情描述
    */
    @ApiModelProperty(value="商品详情描述")
    private String detail;

    /**
    * 商品销量
    */
    @ApiModelProperty(value="商品销量")
    private Integer sellCount;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private Long createTime;

    /**
    * 修改时间
    */
    @ApiModelProperty(value="修改时间")
    private Long updateTime;

    private static final long serialVersionUID = 1L;
}