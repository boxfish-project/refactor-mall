package com.boxfishedu.online.order.controller;

import com.boxfishedu.online.order.entity.OrderForm;
import com.boxfishedu.online.order.service.OrderService;
import com.boxfishedu.online.order.service.RabbitMQService;
import com.boxfishedu.protocal.enums.OrderStatus;
import com.boxfishedu.protocal.model.CommonResult;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.toJsonNoException;
import static com.boxfishedu.protocal.model.CommonResult.createCommonResult;

/**
 * Created by malu on 16/7/11.
 * 支付中心专用接口
 */
@RestController
@RequestMapping("/order")
@Profile({"local", "test", "production.private"})
public class OrderAdminController {
    @Autowired
    OrderService orderService;

    @Autowired
    RabbitMQService rabbitMQService;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(OrderAdminController.class);

    /**
     * 异步修改订单状态为[已支付]
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody OrderForm orderForm) {
        logger.debug("[/order/update] - 请求参数 - [{}]", toJsonNoException(orderForm));
        orderForm.setOrderStatus(OrderStatus.PAID);
        this.rabbitMQService.preUpdateStatus(orderForm);
        logger.debug("[/order/update] - 响应内容 - [orderCode = {}]", orderForm.getOrderCode());

        return createCommonResult(orderForm.getOrderCode());
    }

    /**
     * 支付订单超时后修改订单状态为[关闭]
     */
    @RequestMapping(value = "/closed", method = RequestMethod.POST)
    public CommonResult close(@PathVariable String orderCode){
        OrderForm orderForm = OrderForm.createInstance();
        orderForm.setOrderCode(orderCode);
        orderForm.setOrderStatus(OrderStatus.CLOSED);
        logger.debug("[/order/closed] - 请求参数 - [orderCode = {}]", orderCode);
        this.rabbitMQService.preUpdateStatus(orderForm);
        logger.debug("[/order/closed] - 响应内容 - [orderCode = {}]", orderCode);

        return createCommonResult(orderCode);
    }

    /**
     * 根据订单号获取订单
     */
    @RequestMapping(value = "/byCode/{orderCode}", method = RequestMethod.GET)
    public CommonResult getByCode(@PathVariable String orderCode) {
        logger.debug("[/order/byCode] - 请求参数 - [orderCode = {}]", orderCode);
        OrderForm orderForm = this.orderService.findByCode(orderCode);
        logger.debug("[/order/byCode] - 响应内容 - [{}]", toJsonNoException(orderForm));

        return createCommonResult(orderForm);
    }
}
