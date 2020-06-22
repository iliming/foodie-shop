package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentVo;
import com.imooc.pojo.vo.SearchItemsVo;
import com.imooc.pojo.vo.ShopCartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {
    List<ItemCommentVo> getCommentByItemIdAndLevel(
            @Param("itemId") String itemId, @Param("level") Integer level);


    List<SearchItemsVo> searchItemByKeywords(Map<String, Object> map);

    List<SearchItemsVo> searchItemByCatId(Map<String, Object> map);

    List<ShopCartVo> queryItemsBySpecIds(@Param("paramsList")String specIdsList);

    int decreaseItemSpecStock(@Param("specId")String specId,
                              @Param("pendingCounts")int pendingCounts);
}
