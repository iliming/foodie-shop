package com.imooc.pojo.vo;

import lombok.Data;

/**
 * @author liming
 * @create 2020/6/28
 */
/**
 * 订单状态概览数量VO
 */
@Data
public class OrderStatusCountsVO {
    /**
     * 待支付
     */
    private Integer waitPayCounts;

    /**
     * 待发货
     */
    private Integer waitDeliverCounts;

    /**
     * 待收货
     */
    private Integer waitReceiveCounts;

    /**
     * 待评论
     */
    private Integer waitCommentCounts;
}
