package com.imooc.mapper;

/**
 * @author liming
 * @create 2020/6/28
 */

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.center.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户中心-我的订单自定义 mapper
 */
public interface OrdersMapperCustom {
    /**
     * 我的订单
     * @param map
     * @return
     */
    List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    /**
     * 查询订单数量
     * @param map
     * @return
     */
    int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

    /**
     * 查询订单动向
     * @param map
     * @return
     */
    List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}
