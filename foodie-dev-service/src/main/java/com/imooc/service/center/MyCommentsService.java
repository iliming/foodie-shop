package com.imooc.service.center;

/**
 * @author liming
 * @create 2020/6/28
 */

import com.imooc.pojo.OrderItems;
import com.imooc.pojo.bo.OrderItemsCommentBO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * 评论管理
 */
public interface MyCommentsService {
    /**
     * 根据订单id查询关联的商品评论
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户的评论
     * @param orderId
     * @param userId
     * @param commentList
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);

    /**
     * 我的评价查询 分页
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
