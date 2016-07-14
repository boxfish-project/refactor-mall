package com.boxfishedu.online.order.service;

import com.aliyun.odps.datahub.PackReader;
import com.boxfishedu.online.order.entity.OrderDetail;
import com.boxfishedu.online.order.entity.OrderForm;
import com.boxfishedu.online.order.entity.OrderLog;
import com.boxfishedu.online.order.mappers.OrderDetailMapper;
import com.boxfishedu.online.order.mappers.OrderFormMapper;
import com.boxfishedu.online.order.mappers.OrderLogMapper;
import com.boxfishedu.protocal.exceptions.BusinessException;
import com.boxfishedu.protocal.model.CommonResult;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.One;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.toJson;
import static com.boxfishedu.component.boxfish.util.string.FormatUtil.toJsonNoException;
//import static com.boxfishedu.online.order.entity.OrderForm.OrderChannel.EXPERIENCE;
import static com.boxfishedu.protocal.enums.OrderChannel.EXPERIENCE;
import static com.boxfishedu.protocal.enums.OrderStatus.CHOSE;
import static com.boxfishedu.protocal.enums.OrderStatus.PAID;
import static com.boxfishedu.protocal.enums.OrderStatus.WAIT_PAY;
import static com.boxfishedu.protocal.enums.ServiceType.CHINESE_TEACHER;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * Created by malu on 16/7/11.
 */
@Service
public class OrderService {
    @Autowired
    RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    //产品中心API地址
    @Value("${product-host}")
    private String productHost;

    //支付网关API地址
    @Value("${payment-host}")
    private String paymentHost;

    @Value("${payment-key}")
    private String paymentKey;

    @Value("${edu-center-host}")
    private String eduCenterHost;

    @Value("${invitation-host}")
    private String invitationHost;

    @Autowired
    private OrderFormMapper orderFormMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;
    private String generateOrderCode() {
        int data = new Random().nextInt(90000) + 10000;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        return dateFormat.format(Calendar.getInstance().getTime()) + data;      //毫秒时间戳+5位随机数
    }

    /**
     * 保存订单
     * @param orderForm
     * @return
     * @throws IOException
     */
    public OrderForm save(OrderForm orderForm) throws IOException {
        OrderForm prepareSave = this.pretreat(orderForm);

        OrderLog orderLog = OrderLog.generateOrderLog(prepareSave);

        if(EXPERIENCE.equals(prepareSave.getOrderChannel())){
            logger.info("订单[order_code = {}]是体验订单,需要校验用户是否已创建过体验订单.", prepareSave.getOrderCode());
            if(this.orderFormMapper.countByUserIdAndOrderChannel(prepareSave.getUserId(), EXPERIENCE) > 0){
                logger.error("订单[order_code = {}]创建失败-该用户已经创建过体验订单.", orderForm.getOrderCode());
                throw new BusinessException("创建订单失败 - 该用户已经创建过体验订单,不能再次创建.");
            }
            logger.info("用户[{}]未创建过体验订单,允许创建.", orderForm.getUserId());
            logger.info("订单[order_code = {}]是体验订单,直接发送给支付中心.", orderForm.getOrderCode());
        }
        try{
            if(this.orderFormMapper.insert(prepareSave) == 1){
                orderForm = prepareSave;
                orderLog.setOrderId(orderForm.getId());
                // FIXME: 16/7/13 details实际就一条记录吗?
                OrderDetail detail = orderForm.getOrderDetails().get(0);
                detail.setOrderId(orderForm.getId());
                this.orderLogMapper.insert(orderLog);
                this.orderDetailMapper.insert(detail);
            }
        }catch (Exception e){
            //出现重复订单号,重新生成订单号后保存
            prepareSave.setOrderCode(this.generateOrderCode());
            if(this.orderFormMapper.insert(prepareSave) ==1){
                orderForm = prepareSave;
            }
        }
        orderForm.addOrderLog(orderLog);
        logger.info("[订单order_code = {}已创建]", orderForm.getOrderCode());
        logger.info("[EVENT]create_order{}", toJsonNoException(orderForm));

        return orderForm;
    }

    /**
     * 向支付中心提交
     */
    public OrderForm notifyPayCenter(OrderForm orderForm){

        return null;
    }
    /**
     * 向体验中心提交,获取验证码信息
     */
    public OrderForm notifyInvitation(OrderForm orderForm){
        if(!EXPERIENCE.equals(orderForm.getOrderChannel())){
            return orderForm;
        }

        try{
            Long userId = orderForm.getUserId();
            String orderCode = orderForm.getOrderCode();
            String url = this.invitationHost + "/use/{userId}/{orderCode}";
            logger.debug("调用接口[url = {}]", url);
            logger.debug("向体验资格管理中心发送的请求数据为[userId = {}, orderCode = {}]", userId, orderCode);
            CommonResult result = this.restTemplate.getForObject(url, CommonResult.class, userId, orderCode);
            logger.debug("体验资格管理中心的响应内容为[{}]", url, toJsonNoException(result));
            if(!Objects.isNull(result) && result.getReturnCode() == HttpStatus.OK.value()
                    && "success".contentEquals(result.getReturnMsg())){
                return orderForm;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("订单[{}]创建失败 - 体验资格管理中心状态异常.", orderForm.getOrderCode());
        }
        this.orderFormMapper.deleteByPrimaryKey(orderForm.getId());
        this.orderDetailMapper.deleteOrderDatails(orderForm.getId());
        this.orderLogMapper.deleteOrderLogs(orderForm.getId());

        return null;
    }

    /**
     * 预处理订单
     * @param prepareSave
     * @return
     */
    private OrderForm pretreat(OrderForm prepareSave) throws IOException{
        //防止无效数据存入数据库,新建对象后再存储
        OrderForm orderForm = OrderForm.createInstance();
        //生成OrderCode
        String orderCode = generateOrderCode();
        //订单子项内容校验
        logger.info("订单[code = {}]开始检查.", orderCode);

        orderForm.setOrderCode(orderCode);
        orderForm.setCreateTime(Calendar.getInstance().getTime());
        orderForm.setOrderStatus(WAIT_PAY);
        orderForm.setOrderChannel(prepareSave.getOrderChannel());
        orderForm.setOrderSource(prepareSave.getOrderSource());
        orderForm.setUserId(prepareSave.getUserId());
        orderForm.setRemark(prepareSave.getRemark());

        OrderDetail orderDetail = prepareSave.getOrderDetails().get(0);
        Map combo = this.checkProduct(orderDetail);

        orderDetail.setCreateTime(orderForm.getCreateTime());
        orderDetail.setComboCycle(combo.get("skuCycle").toString());
        orderDetail.setCountInMonth(combo.get("skuAmount").toString());
        Map serviceSku = (Map) combo.get("serviceSKU");
        orderDetail.setProductName(serviceSku.get("skuName").toString());
        String jsonCombo = toJson(combo);
        jsonCombo.replace(CHINESE_TEACHER.name(), CHINESE_TEACHER.ordinal() + "");
        orderDetail.setProductInfo(jsonCombo);

        orderForm.addOrderDetail(orderDetail);

        orderForm.setOrderFee(orderDetail.getPrice());
        if(EXPERIENCE.equals(orderForm.getOrderChannel())){
            orderForm.setCouponFee(orderDetail.getPrice());
        }else {
            orderForm.setCouponFee(0l);//无其他优惠活动,优惠价格为0元
        }
        orderForm.setPayFee(orderForm.getOrderFee() - orderForm.getCouponFee());//实际付款为:选课价格 - 优惠价格

        logger.info("订单[code = {},order_fee = {}]检查完毕.", orderCode, orderForm.getOrderFee());
        return orderForm;
    }

    /**
     * 调用服务中心接口检查提交的订单项是否正确
     * 是-返回服务内容
     * 否-阻止订单提交
     */
    private Map checkProduct(OrderDetail detail) {
        Long productId = detail.getProductId();
        Long price = detail.getPrice();

        try{
            String url = this.productHost + "/s-mall/check/{productId}/{price}";
            CommonResult object = this.restTemplate.getForObject(url, CommonResult.class, productId, price);
            logger.info("订单项[combo_id = {}]检查通过,可以使用.", detail.getProductId());

            return (Map) object.getData();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("订单项[sku_combo_id = {},price = {}]不存在或价格错误,创建订单失败.", productId, detail.getPrice());
            throw new BusinessException("订单创建失败 - 订单项[sku_combo_id = " + productId + "," +
                    "price = " + detail.getPrice() + "]不存在或价格错误!");
        }
    }

    /**
     * 检查用户是否有体验资格:
     * 1.在免费体验邀请名单中;e
     * 2.未曾创建过免费体验订单;
     * 3.积分超过10万
     * <p>
     * 三个条件有一个不通过均不能创建免费体验订单
     */
    public Map<String, Object> checkExperienced(Long userId, Integer score){
        Map<String, Object> result = newHashMap();
        result.put("score", score);
        logger.info("体验资格检查 - [userId = {}, 用户积分 = {}]", userId, score);

        Integer count = this.orderFormMapper.countByUserIdAndOrderChannel(userId, EXPERIENCE);
        result.put("experienced", count > 0);//为true表示已经创建过体验订单
        logger.info("体验资格检查 - [userId = {}, 是否已经创建过体验订单 = {}]", userId, count > 0);

        String url = this.invitationHost + "/check/{userId}";
        logger.debug("[{}]请求参数    -   [userId = {}]", url, userId);
        CommonResult checkResult = this.restTemplate.getForObject(url, CommonResult.class, userId);
        Boolean canExp = false;
        if(!Objects.isNull(checkResult)){
            logger.debug("[{}]响应内容    -   [{}]", url, toJsonNoException(checkResult));
            canExp = Boolean.valueOf(checkResult.getData().toString());
        }
        logger.info("体验资格检查 - [userId = {},是否在免费体验邀请名单中 = {}]", userId, canExp);
        result.put("qualified", canExp);

        return result;
    }

    public boolean findUnFinishedCount(Long userId){
        Example example = new Example(OrderForm.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andIn("orderStatus", newArrayList(PAID, WAIT_PAY));

        return this.orderFormMapper.selectCountByExample(example) > 0;
    }

    public OrderForm findByCode(String orderCode, Long userId){
        OrderForm one = OrderForm.createInstance();
        one.setOrderCode(orderCode);
        one.setUserId(userId);
        OrderForm orderForm = this.orderFormMapper.selectOne(one);
        if(!Objects.isNull(orderForm)){
            orderForm.setOrderDetails(this.getDetailsByOrderId(orderForm.getId()));
        }
        return orderForm;
    }

    public OrderForm findByCode(String orderCode){
        OrderForm one = OrderForm.createInstance();
        one.setOrderCode(orderCode);
        OrderForm orderForm = this.orderFormMapper.selectOne(one);
        if(!Objects.isNull(orderForm)){
            orderForm.setOrderDetails(this.getDetailsByOrderId(orderForm.getId()));
        }
        return orderForm;
    }

    public OrderForm findById(Long orderId, Long userId){
        OrderForm one = OrderForm.createInstance();
        one.setId(orderId);
        one.setUserId(userId);
        OrderForm orderForm = this.orderFormMapper.selectOne(one);
        if(!Objects.isNull(orderForm)){
            orderForm.setOrderDetails(this.getDetailsByOrderId(orderId));
        }
        return orderForm;
    }

    public List<OrderForm>findUnDroppedList(OrderForm orderForm){
        List<OrderForm>list = this.findList(orderForm);
        for (OrderForm form: list) {
            if(form.getOrderStatus().equals(CHOSE)){
                //若订单状态为已选课,需向教学中心获取服务次数剩余情况并暂存至remark字段
                form.setRemark(this.getEduInfo(orderForm.getId()));
            }
            //添加订单详情信息及日志信息
            form.setOrderDetails(this.getDetailsByOrderId(form.getId()));
            form.setOrderLogs(this.getLogsByOrderId(form.getId()));
        }
        return list;
    }

    /**
     *分页查找
     * @param orderForm
     * @return
     */
    private List<OrderForm>findList(OrderForm orderForm){
        if(!Objects.isNull(orderForm.getPage()) && !Objects.isNull(orderForm.getRows())){
            PageHelper.startPage(orderForm.getPage(), orderForm.getRows());
        }
        Example example = new Example(OrderForm.class);
        Example.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(orderForm.getUserId())){
            criteria.andEqualTo("userId", orderForm.getUserId());
        }

        example.setOrderByClause("create_time desc");
        return this.orderFormMapper.selectByExample(example);
    }

    private String getEduInfo(Long orderId){
        try{
            String url = eduCenterHost + "/service/order/{orderId}/info";
            logger.debug("[{}]获取服务信息 -  请求参数 [order_id = {}]", url, orderId);
            CommonResult object = this.restTemplate.getForObject(url, CommonResult.class, orderId);
            logger.debug("[{}]响应内容 - {}", url, toJsonNoException(object));
            return toJsonNoException(object.getData());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("服务中心错误 - 未能获取订单[order_id = {}]的服务情况.", orderId);
            return "暂未获取";
        }
    }

    /**
     * 查询对应订单的详情信息
     * @param orderId
     * @return
     */
    private List<OrderDetail> getDetailsByOrderId(Long orderId){
        Example example = new Example(OrderDetail.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("orderId", orderId);

        example.setOrderByClause("create_time desc");
        return this.orderDetailMapper.selectByExample(example);
    }

    private List<OrderLog> getLogsByOrderId(Long orderId){
        Example example = new Example(OrderLog.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("orderId", orderId);

        example.setOrderByClause("create_time desc");
        return this.orderLogMapper.selectByExample(example);
    }

}
