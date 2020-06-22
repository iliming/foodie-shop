package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import com.imooc.utils.BaseResult;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.imooc.controller.BaseController.FOODIE_SHOPCART;
import static com.imooc.controller.BaseController.payReturnUrl;

@Api(value = "订单相关",tags={"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController {
    @Autowired
    private OrderService orderService;


    @ApiOperation(value = "支付中心创建订单",notes = "支付中心创建订单",httpMethod = "POST")
    @PostMapping("create")
    public BaseResult create(@RequestBody SubmitOrderBO submitOrderBO,
                             HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s = JsonUtils.objectToJson(submitOrderBO);
        if(submitOrderBO.getPayMethod()!= PayMethod.WEIXIN.type &&
                submitOrderBO.getPayMethod()!= PayMethod.ALIPAY.type){
            return BaseResult.errorMsg("支付方式不支持");
        }
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART +":" + submitOrderBO.getUserId());
        if(StringUtils.isBlank(shopCartJson)){
            return BaseResult.errorMsg("购物车为空");
        }
        List<ShopcartBO> list = JsonUtils.jsonToList(shopCartJson, ShopcartBO.class);
        //1.创建订单
        OrderVO orderVO = null;
        try {
            orderVO = orderService.creatOrder(list,submitOrderBO);
        } catch (Exception e) {
            return BaseResult.errorMsg("扣减库存出错，库存不足！");
        }
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);
        //便于测试 这边都以一分钱
        merchantOrdersVO.setAmount(1);
        //2.创建订单以后，移除购物车中已结算（已提交的商品）
        list.removeAll(orderVO.getToBeRemovedShopCartList());
        redisOperator.set(FOODIE_SHOPCART +":" + submitOrderBO.getUserId(),JsonUtils.objectToJson(list));
        CookieUtils.setCookie(request,response,FOODIE_SHOPCART,JsonUtils.objectToJson(list),true);
        //3.向支付中心发送当前的订单，用于保存支付中心的数据
        //1.可以使用Http.post 2.用spring集成的http请求
        //1.Http.post
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse responsep = null;
//        String url = paymentUrl;
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Content-Type", "application/json");
//        post.addHeader("imoocUserId","imooc");
//        post.addHeader("password","imooc");
//        String s = JsonUtils.objectToJson(merchantOrdersVO);
//        StringEntity stringEntity = new StringEntity(s);
//        post.setEntity(stringEntity);
//        responsep = client.execute(post);
//        String	result = EntityUtils.toString(responsep.getEntity(),"UTF-8");
//        System.out.println(result);
        //2.spring集成的http
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","imooc");
        headers.add("password","imooc");
        //入参的格式
        HttpEntity<MerchantOrdersVO> httpEntity = new HttpEntity<MerchantOrdersVO>(merchantOrdersVO,headers);
        //返回的参数格式
        ResponseEntity<BaseResult> resultResponseEntity = restTemplate.postForEntity(paymentUrl,httpEntity, ResponseJSONResult.class);
        BaseResult result = resultResponseEntity.getBody();
        if(result.getStatus() != 200){
            BaseResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }
        return BaseResult.ok(merchantOrdersVO.getMerchantOrderId());
    }

}
