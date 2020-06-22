package com.imooc.service;

import com.imooc.pojo.*;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.CommentLevelCountsVo;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.pojo.vo.ShopCartVo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface OrderService {
    /**
     * 生成订单
     * @param list
     * @param submitOrderBO
     */
    public OrderVO creatOrder(List<ShopcartBO> list, SubmitOrderBO submitOrderBO) throws Exception;

    /**
     * 根据订单id  改变状态为已支付，待发货
     * @param merchantOrderId
     * @param waitDeliver
     */
    void updateOrderStatus(String merchantOrderId, int waitDeliver);

    /**
     * 根据订单id获取订单状态
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    void closeOrder();

}
