package com.boxfishedu.online.order.controller;

import com.boxfishedu.online.order.entity.OrderForm;
import com.boxfishedu.online.order.service.OrderService;
import com.boxfishedu.protocal.exceptions.BusinessException;
import com.boxfishedu.protocal.model.CommonResult;
import com.boxfishedu.protocal.premission.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.toJsonNoException;
import static com.boxfishedu.protocal.model.CommonResult.createCommonResult;

/**
 * Created by malu on 16/7/11.
 */
@RestController
@RequestMapping("/order/app")
@Profile({"local","test","production.public"})
public class OrderAppController {

    @Autowired
    OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(OrderForm.class);

    /**
     * 按用户id分页查找包含订单项的订单列表,默认返回10条数据,安创建时间倒序排列
     * @param pageNum 当前页号
     * @param userId 用户id
     * @return
     */
    @RequestMapping("/page/{pageNum}")
    public CommonResult page(@PathVariable Integer pageNum, @RequestParam Long userId){

        OrderForm orderForm = OrderForm.createInstance();
        orderForm.setPage(pageNum);
        orderForm.setUserId(userId);

        logger.debug("[/order/app/page] - 请求参数 - [pageNum = {},user_id = {}]", pageNum, userId);
        List<OrderForm> unDroppedList = this.orderService.findUnDroppedList(orderForm);
        logger.debug("[/order/app/page] - 响应内容 - [{}]", toJsonNoException(unDroppedList));
        return createCommonResult(unDroppedList);
    }

    /**
     * 根据订单ID查询包含订单项,订单支付信息的订单
     * 所有枚举类型的字段均直接返回真实数值,不做视图转换处理
     */
    @RequestMapping("/detail/{orderId}")
    public CommonResult detail(@PathVariable Long orderId, HttpServletRequest request){
        Long userId = Long.valueOf(request.getParameter("userId"));
        logger.debug("[/order/app/detail] - 请求参数 [orderId = {}, userId = {}]", orderId, userId);
        OrderForm orderForm = this.orderService.findById(orderId, userId);
        logger.debug("[/order/app/detail] - 响应内容 - [{}]", toJsonNoException(orderForm));
        return createCommonResult(orderForm);
    }

    /**
     * 根据订单号orderCode查询包含订单项,订单支付信息的订单
     */
    @RequestMapping("/byCode/{orderCode}")
    public CommonResult byCode(@PathVariable String orderCode, @RequestParam Long userId){
        logger.debug("[/order/app/byCode] - 请求参数 [orderId = {}, userId = {}]", orderCode, userId);
        OrderForm orderForm = this.orderService.findByCode(orderCode, userId);
        logger.debug("[/order/app/byCode] - 响应内容 - {}", toJsonNoException(orderForm));
        return createCommonResult(orderForm);
    }

    /**
     * 创建订单并返回持久化的订单数据,不做视图转换处理
     * 提交的数据中需要包含订单项和支付渠道信息
     * 用于APP端创建订单,使用OrderChannel区分订单来源
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody OrderForm orderForm, HttpServletRequest request) throws IOException {
//        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
//        orderForm.setUserId(userInfo.getId());
//        //FIXME 创建免费订单时需要把access_token传给支付中心,暂时先用remark字段存放
//        orderForm.setRemark(userInfo.getAccess_token());

        orderForm.setUserId(1298782l);

        logger.debug("/order/app/create - 请求参数 [{}]", toJsonNoException(orderForm));
        orderForm = this.createOrderForm(orderForm);
        logger.debug("/order/app/create - 响应内容 [{}]", toJsonNoException(orderForm));
        return createCommonResult(orderForm);
    }

    //FIXME save方法在一个事务中,这个方法必须与save不在同一个Bean中否则事务无效
    private OrderForm createOrderForm(OrderForm orderForm) throws IOException {
        OrderForm order = this.orderService.save(orderForm);

        //若为体验订单,向支付中心发送请求

        order = this.orderService.notifyInvitation(order);
        if(Objects.isNull(order)){
            throw new BusinessException("创建订单失败 - 体验资格管理中心服务异常!");
        }

        return order;
    }


    /**
     * 查询用户是否有未支付或未选课订单
     */
    @RequestMapping(value = "/unfinished", method = RequestMethod.GET)
    public CommonResult unfinished(@RequestParam Long userId){
        logger.debug("[/order/app/unfinished] - 请求参数 - [userId = {}]", userId);
        Boolean unfinished = this.orderService.findUnFinishedCount(userId);
        logger.debug("[/order/app/unfinished] - 响应内容 - [{}]", toJsonNoException(unfinished));
        return createCommonResult(unfinished);
    }

    /**
     * 根据用户id查询该用户是否已经使用了免费体验机会
     */
    @RequestMapping(value = "/experienced", method = RequestMethod.GET)
    public CommonResult experienced(HttpServletRequest request){
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        Long userId = 1298782l;//userInfo.getId();
        Integer score = 200000;//userInfo.getScore();
        logger.debug("[/order/app/experienced] - 邀请参数 - [userId = {}, score = {}]", userId, score);
        Map<String, Object> result = this.orderService.checkExperienced(userId, score);
        logger.debug("[/order/app/experienced] - 响应内容 - [{}]", toJsonNoException(request));

        return createCommonResult(result);
    }
}
