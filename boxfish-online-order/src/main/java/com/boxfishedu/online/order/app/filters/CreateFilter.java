package com.boxfishedu.online.order.app.filters;

import com.boxfishedu.online.order.app.util.BodyReaderRequestWrapper;
import com.boxfishedu.online.order.app.util.HttpHelper;
import com.boxfishedu.online.order.entity.OrderDetail;
import com.boxfishedu.online.order.entity.OrderForm;
import com.boxfishedu.protocal.enums.OrderChannel;
import com.boxfishedu.protocal.enums.OrderSource;
import com.boxfishedu.protocal.exceptions.InvalidInputResponseBuilder;
import com.boxfishedu.protocal.premission.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.fromJson;
//import static com.boxfishedu.online.order.entity.OrderForm.OrderChannel.EXPERIENCE;
import static com.boxfishedu.protocal.enums.OrderChannel.EXPERIENCE;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by lauzhihao on 2016/05/17.
 * 用于过滤创建订单时的数据校验,涉及较多的枚举,所以不使用spring validation
 */
// FIXME: 16/7/12 验证路径为/order/app/create 现测试时,改为/order/apps/create
@WebFilter(urlPatterns = "/order/apps/create", initParams = @WebInitParam(name = "init", value = "1"))
public class CreateFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(CreateFilter.class);

    @Value("${conf.benchmark.score}")
    private Integer score;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 输入流读取一次后就没有了, 所以需要将流写回去
        ServletRequest requestWrapper = new BodyReaderRequestWrapper(request);
        String requestBody = HttpHelper.getBodyString(requestWrapper);
        OrderForm orderForm = fromJson(requestBody, OrderForm.class);

        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        try {
            String key = "processing-user-" + userInfo.getAccess_token();

            if(this.redisTemplate.hasKey(key)){
                InvalidInputResponseBuilder.build(response, "请勿频繁操作!");
                return;
            }

            String oldValue = this.redisTemplate.opsForValue().getAndSet(key, userInfo.getAccess_token());
            this.redisTemplate.expire(key, 5, TimeUnit.SECONDS);

            if (!isEmpty(oldValue)) {
                InvalidInputResponseBuilder.build(response, "请勿频繁操作!");
                return;
            }

            logger.info("订单[order_code = {}] - 访问频率控制已开启.", userInfo.getAccess_token());
        } catch (Exception e) {
            logger.error("Redis服务异常,访问频率控制已失效.");
        }

        //检查各参数是否符合业务逻辑
        if (!OrderChannel.contains(orderForm.getOrderChannel())) {
            logger.warn("订单创建失败 - 无效的OrderChannel");
            InvalidInputResponseBuilder.build(response, "订单创建失败 - 无效的OrderChannel");
            return;
        }

        if (!OrderSource.contains(orderForm.getOrderSource())) {
            logger.warn("订单创建失败 - 无效的OrderSource");
            InvalidInputResponseBuilder.build(response, "订单创建失败 - 无效的OrderSource");
            return;
        }

        List<OrderDetail> orderDetails = orderForm.getOrderDetails();

        if (isNull(orderDetails) || orderDetails.size() != 1) {
            logger.warn("订单创建失败 - 无效的orderDetail");
            InvalidInputResponseBuilder.build(response, "订单创建失败 - 无效的orderDetail");
            return;
        }
        OrderDetail orderDetail = orderDetails.get(0);

        if (EXPERIENCE.equals(orderForm.getOrderChannel())) {
            if (isNull(userInfo.getScore()) || userInfo.getScore() < this.score) {
                logger.warn("免费体验订单创建失败 - 积分不足");
                InvalidInputResponseBuilder.build(response, "免费体验订单创建失败 - 积分不足");
                return;
            }

            if (orderDetail.getProductId() != 1 || orderDetail.getPrice() != 0) {
                logger.warn("免费体验订单创建失败 - 无效的订单项");
                InvalidInputResponseBuilder.build(response, "免费体验订单创建失败 - 无效的订单项");
                return;
            }
        }

        try {
            if (isNull(orderDetail.getProductId()) || orderDetail.getAmount() != 1
                    || isNull(orderDetail.getPrice())) {
                logger.warn("订单创建失败 - 无效的orderDetail");
                InvalidInputResponseBuilder.build(response, "订单创建失败 - 无效的orderDetail");
                return;
            }
        } catch (Exception e) {
            logger.warn("订单创建失败 - 无效的orderDetail");
            InvalidInputResponseBuilder.build(response, "订单创建失败 - 无效的orderDetail");
            return;
        }

        filterChain.doFilter(requestWrapper, response);
    }
}
