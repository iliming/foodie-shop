package com.imooc.pojo.bo;

import lombok.Data;

/**
 * @author liming
 * @create 2020/6/28
 */
@Data
public class OrderItemsCommentBO {
    /**
     * 评论 Id
     */
    private String commentId;

    /**
     * 商品 Id
     */
    private String itemId;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品规格 Id
     */
    private String itemSpecId;

    /**
     * 商品规格名称
     */
    private String itemSpecName;

    /**
     * 评论等级
     */
    private Integer commentLevel;

    /**
     * 评论内容
     */
    private String content;
}
