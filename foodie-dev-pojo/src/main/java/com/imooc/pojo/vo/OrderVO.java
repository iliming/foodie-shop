package com.imooc.pojo.vo;

import com.imooc.pojo.bo.ShopcartBO;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {
    private String orderId;

    private MerchantOrdersVO merchantOrdersVO;

    private List<ShopcartBO> toBeRemovedShopCartList;
}
