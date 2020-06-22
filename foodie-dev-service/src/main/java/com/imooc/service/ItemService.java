package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVo;
import com.imooc.pojo.vo.ShopCartVo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品id查询商品信息
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);
    /**
     * 根据商品id查询出对应的图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);
    /**
     * 根据商品id查询出商品的规格信息
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);
    /**
     * 根据商品id查询出商品的参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);
    /**
     *根据商品id查询评价数量
     * @param itemId
     * @return
     */
    CommentLevelCountsVo queryCommentCounts(String itemId);
    /**
     * 根据评价等级和商品id获取评价内容
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult getCommentByItemIdAndLevel(String itemId, Integer level, Integer page, Integer pageSize);
    /**
     * 检索商品
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemByKeywords(String keywords, String sort, Integer page, Integer pageSize);
    /**
     * 查询类别下的所有商品 分页
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemByCatId(String catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格id获取商品规格信息
     * @param specIds
     * @return
     */
    List<ShopCartVo> queryItemsBySpecIds(String specIds);

    ItemsSpec queryItemsBySpecId(String specId);

    String queryItemMainImgById(String itemId);

    /**
     * 下单之后扣减库存
     * @param specId
     * @param buyCounts
     */
    void decreaseItemSpecStock(String specId,int buyCounts);
}
