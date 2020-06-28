package com.imooc.controller.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.BaseResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author liming
 * @create 2020/6/28
 */
@Api(value = "用户中心我的订单", tags = {"用户中心我的订单相关接口"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController {
    private static final int COMMON_PAGE_SIZE = 10;

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "获得订单状态数", notes = "获得订单状态数", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public BaseResult statusCounts(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return BaseResult.errorMsg(null);
        }
        OrderStatusCountsVO result = myOrdersService.getOrderStatusCounts(userId);
        return BaseResult.ok(result);
    }

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public BaseResult query(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                                  @ApiParam(name = "orderStatus", value = "订单状态") @RequestParam Integer orderStatus,
                                  @ApiParam(name = "page", value = "查询下一页的第几页") @RequestParam Integer page,
                                  @ApiParam(name = "pageSize", value = "分页的每一页显示的条数") @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return BaseResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return BaseResult.ok(grid);
    }

    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value="商家发货", notes="商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public BaseResult deliver(@ApiParam(name = "orderId", value = "订单id", required = true) @RequestParam String orderId) throws Exception {
        if (StringUtils.isBlank(orderId)) {
            return BaseResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return BaseResult.ok();
    }

    @ApiOperation(value="用户确认收货", notes="用户确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public BaseResult confirmReceive(@ApiParam(name = "orderId", value = "订单id", required = true) @RequestParam String orderId,
                                           @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) throws Exception {

        BaseResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return BaseResult.errorMsg("订单确认收货失败！");
        }

        return BaseResult.ok();
    }

    @ApiOperation(value="用户删除订单", notes="用户删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public BaseResult delete(@ApiParam(name = "orderId", value = "订单id", required = true) @RequestParam String orderId,
                                   @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {

        BaseResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.deleteOrder(userId, orderId);
        if (!res) {
            return BaseResult.errorMsg("订单删除失败！");
        }

        return BaseResult.ok();
    }

    /**
     * 用于验证用户和订单是否有关联关系(防止非法用户调用)
     * @param userId
     * @param orderId
     * @return
     */
    private BaseResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return BaseResult.errorMsg("订单不存在！");
        }
        return BaseResult.ok();
    }

    @ApiOperation(value = "查询订单动向", notes = "查询订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public BaseResult trend(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                                  @ApiParam(name = "page", value = "查询下一页的第几页") @RequestParam Integer page,
                                  @ApiParam(name = "pageSize", value = "分页的每一页显示的条数") @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return BaseResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = myOrdersService.getOrdersTrend(userId, page, pageSize);
        return BaseResult.ok(grid);
    }

}
