package com.boxfishedu.online.order.app.filters;

import com.boxfishedu.online.order.app.util.BodyReaderRequestWrapper;
import com.boxfishedu.protocal.exceptions.InvalidInputResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.getProperty;
import static com.boxfishedu.online.order.app.util.HttpHelper.getBodyString;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@WebFilter(urlPatterns = "/order/update")
public class UpdateFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(UpdateFilter.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // POST输入流读取一次后就没有了, 所以需要将流继续写出去
        ServletRequest requestWrapper = new BodyReaderRequestWrapper(request);
        String requestBody = getBodyString(requestWrapper);

        if (isEmpty(requestBody)) {
            InvalidInputResponseBuilder.build(response, "无效的订单.");
            return;
        }

        String code = getProperty(requestBody, "orderCode");
        if (isEmpty(code)) {
            InvalidInputResponseBuilder.build(response, "无效的订单.");
            return;
        }

//        try {
//            String key = "processing.order." + code;
//            if (this.stringRedisTemplate.hasKey(key)) {
//                InvalidInputResponseBuilder.build(response, "该订单正在处理中,请稍后...");
//                logger.warn("订单[order_code = {}]正在处理中或在冷却中,直接返回.", code);
//                return;
//            }
//
//            //每条订单在2分钟内只能请求1次
//            this.stringRedisTemplate.opsForValue().set(key, code, 2, TimeUnit.MINUTES);
//
//            logger.info("订单[order_code = {}] - 并发锁定已开启", code);
//        } catch (Exception e) {
//            logger.error("Redis服务异常,并发锁失效中...");
//        }

        filterChain.doFilter(requestWrapper, response);
    }
}
