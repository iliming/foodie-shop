package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.ItemsSpecMapper;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final String orderZookeeperKey = "order";
    private final String zookeeperCuratorPath = "/order";

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private AddressService addressService;
    @Autowired
    private Sid sid;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String addressId = submitOrderBO.getAddressId();
        int payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        //1.订单实体新增
        String orderId = sid.nextShort();
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());
        orders.setLeftMsg(leftMsg);
        orders.setPayMethod(payMethod);
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setIsComment(YesOrNo.NO.type);
        orders.setUserId(userId);
        //邮费设置为0
        int postAmount = 0;
        orders.setPostAmount(postAmount);
        //收件人姓名 需要从地址信息表取 条件userId,addressId
        UserAddress userAddress = addressService.queryUserAddress(userId,addressId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getProvince()+" "+userAddress.getCity()
                +" "+userAddress.getDistrict()+" "+userAddress.getDetail());
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        //由于还没用到Redis 这里的购物车的商品数量先用1
        // TODO 整合Redis后，商品购买数量重新从Redis的购物车中获取
        int buyCount = 1;
        // 2. 循环根据itemsSpecIds保存订单商品信息
        String[] specids = itemSpecIds.split(",");
        List<ShopcartBO> toBeRemovedShopCarts = new ArrayList<>();
        for(String specId : specids){
            //2.1 根据规格id 查询规格的具体信息 查出对应的价格
            ItemsSpec itemsSpec = itemService.queryItemsBySpecId(specId);
            totalAmount += itemsSpec.getPriceNormal() * buyCount;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCount;
            //2.2 根据商品id 获取商品以及商品图片
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String url = itemService.queryItemMainImgById(itemId);
            //2.3 循环保存订单详情数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems orderItems = new OrderItems();
            orderItems.setBuyCounts(buyCount);
            orderItems.setItemId(itemId);
            orderItems.setId(subOrderId);
            orderItems.setOrderId(orderId);
            orderItems.setItemSpecId(specId);
            orderItems.setItemName(items.getItemName());
            orderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItems.setItemImg(url);
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItemsMapper.insert(orderItems);
            // 2.4 在用户提交订单后，规格表中的库存要扣除
            itemService.decreaseItemSpecStock(specId,buyCount);

            //都可以实现集群部署定时任务加锁
            // 加redis分布式锁
//            getRedisLock(specId,buyCount);
            // 加zookeeper分布式锁
            //getZookeeperLock(specId,buyCount);
            // 加zookeeper Curator分布式锁
//            getZookeeperCurator(specId,buyCount);
            //getRedissonLock(specId,buyCount);
        }
        //保存订单表到数据库
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(orders);
        //3.保存订单状态表
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreatedTime(new Date());
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatusMapper.insert(orderStatus);
        //4.构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setPayMethod(payMethod);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        //5. 构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        return orderVO;

    }



    //支持阻塞  Redis实现
//    private void getRedissonLock(String specId,Integer buyCount) throws Exception {
//        RedissonClient redisson = RedissonUtils.getRedissonClient();
//        RLock rLock = redisson.getLock(orderZookeeperKey);
//        log.info("我进入了方法！！");
//        try {
//            rLock.lock(30, TimeUnit.SECONDS);
//            log.info("我获得了锁！！！");
//            itemService.decreaseItemSpecStock(specId,buyCount);
////            Thread.sleep(10000);
//        }finally {
//            log.info("我释放了锁！！");
//            rLock.unlock();
//        }
//        log.info("方法执行完成！！");
//    }

    // 从获取锁到释放锁基本很快 如果出现并发也可以确保数据的原子性
    // 缺点 不支持阻塞 就是并发下单的时候 后面那位不会等待前面那个释放锁，直接执行后面的
    // 缺点 redis 默认key失效时间30s 假设超过30s 就会存在key被自动释放
    // 下一个进来可能就会出现其他的问题（库存可能不充足但还是下单成功了）
//    private void getRedisLock(String specId,Integer buyCount) throws Exception{
//        try (RedisLock redisLock = new RedisLock(redisTemplate,"redisKey",15)){
//            if (redisLock.getLock()) {
//                log.info("我进入了锁！！");
//                itemService.decreaseItemSpecStock(specId,buyCount);
//                Thread.sleep(16000);
//            }
//        }
//    }



    //支持阻塞  zookeeper实现
//    private void getZookeeperLock(String specId,Integer buyCount) throws Exception {
//        log.info("我进入了方法！");
//        try(ZkLock zkLock = new ZkLock()) {
//            if (zkLock.getLock(orderZookeeperKey)){
//                log.info("我获得了锁");
//                itemService.decreaseItemSpecStock(specId,buyCount);
//            }
//        }
//        £log.info("方法执行完成！");
//    }

    //支持阻塞  zookeeperCurator实现
//    private void getZookeeperCurator(String specId,Integer buyCount) throws Exception {
//        log.info("我进入了方法！");
//        InterProcessMutex lock = new InterProcessMutex(client, zookeeperCuratorPath);
//        try{
//            if (lock.acquire(30, TimeUnit.SECONDS)){
//                log.info("我获得了锁！！");
//                itemService.decreaseItemSpecStock(specId,buyCount);
//                Thread.sleep(15000);
//            }
//        } finally {
//            try {
//                log.info("我释放了锁！！");
//                lock.release();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        log.info("方法执行完成！");
//    }

    private ShopcartBO getBuyCountFromRedis(List<ShopcartBO> list,String specId) {
        for(ShopcartBO bo : list){
            if(bo.getSpecId().equals(specId)){
                return bo;
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String merchantOrderId, Integer waitDeliver) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(merchantOrderId);
        orderStatus.setPayTime(new Date());
        orderStatus.setOrderStatus(waitDeliver);
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional
    @Override
    public void closeOrder() {
        //1.查询出订单状态表中 1天内未付款的用户
        OrderStatus os = new OrderStatus();
        os.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatuses = orderStatusMapper.select(os);
        for(OrderStatus orderStatus : orderStatuses){
            Date createdTime = orderStatus.getCreatedTime();
            Date currentTime = new Date();
            int daysBetween = DateUtil.daysBetween(createdTime, currentTime);
            if(daysBetween >= 1){
                updateOrder(orderStatus.getOrderId());
            }
        }
    }

    /**
     * 根据订单id 定时任务修改订单状态为交易取消
     */
    private void updateOrder(String orderId){
        //1.订单状态表状态修改
        OrderStatus os = new OrderStatus();
        os.setOrderId(orderId);
        os.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatusMapper.updateByPrimaryKeySelective(os);
        //2.查询订单中下单的商品
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        List<OrderItems> oi = orderItemsMapper.select(orderItems);
        for(OrderItems orderItem : oi){
            //3.获取他的规格id
            int buyCounts = orderItem.getBuyCounts();
            String itemSpecId = orderItem.getItemSpecId();
            //4.去查询他的库存 更新库存
            ItemsSpec itemsSpec = itemsSpecMapper.selectByPrimaryKey(itemSpecId);
            //5.库存加上取消订单的数量
            int count =  itemsSpec.getStock() + buyCounts;
            itemsSpec.setStock(count);
            itemsSpecMapper.updateByPrimaryKey(itemsSpec);
        }
    }
}
