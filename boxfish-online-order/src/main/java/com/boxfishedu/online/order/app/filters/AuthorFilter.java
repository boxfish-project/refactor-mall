package com.boxfishedu.online.order.app.filters;




import com.boxfishedu.protocal.exceptions.InvalidInputResponseBuilder;
import com.boxfishedu.protocal.premission.UserInfo;
import com.boxfishedu.protocal.premission.UserInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by lauzhihao on 2016/05/17.
 * 拦截所有APP端调用的接口,校验用户是否有效
 * 因为它是A开头,所以比C开头的先执行 - [亲测有效]
 */
//// FIXME: 16/7/12 验证路径为/order/app/* 现测试时,改为/order/apps/*
@WebFilter(urlPatterns = "/order/apps/*")
public class AuthorFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(AuthorFilter.class);

    @Autowired
    private UserInfoValidator userInfoValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //没有access_token的请求直接返回
        String accessToken = request.getParameter("access_token");

        if (isEmpty(accessToken) || (accessToken.length() != 10)) {
            InvalidInputResponseBuilder.unauthorized(response, "无效的用户标识,请勿越权使用.");
            return;
        }

        //将access_token转换为userId,如果返回null则表示用户不存在,直接返回
        UserInfo userInfo = this.userInfoValidator.getUserInfo(accessToken);
        if (isNull(userInfo)) {
            InvalidInputResponseBuilder.unauthorized(response, "无效的用户标识,请勿越权使用.");
            return;
        }
        //logger.info("请求接口 = [{}],access_token = [{}],userId = [{}]", request.getRequestURI(), accessToken, userInfo.getId());
        //如果是创建订单操作,还需要把用户信息放进request
        if (request.getRequestURI().contains("create") || request.getRequestURI().contains("experienced")) {
            request.setAttribute("userInfo", this.userInfoValidator.getUserInfo(accessToken));

            filterChain.doFilter(request, response);
        } else {
            Map<String, Object> params = newHashMap();
            params.put("userId", userInfo.getId());

            AuthorRequestWrapper authorRequestWrapper = new AuthorRequestWrapper(request, params);
            filterChain.doFilter(authorRequestWrapper, response);
        }
    }

    private class AuthorRequestWrapper extends HttpServletRequestWrapper {
        private Map<String, String[]> params = newHashMap();

        AuthorRequestWrapper(HttpServletRequest request) {
            super(request);
            this.params.putAll(request.getParameterMap());
            this.modifyParameterValues();
        }

        //重载一个构造方法
        AuthorRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
            this(request);
            addAllParameters(extendParams);//这里将扩展参数写入参数表
        }

        void modifyParameterValues() {//将parameter的值去除空格后重写回去
            Set<String> set = params.keySet();
            for (String key : set) {
                String[] values = params.get(key);
                values[0] = values[0].trim();
                params.put(key, values);
            }
        }

        @Override
        public String getParameter(String name) {//重写getParameter，代表参数从当前类中的map获取
            String[] values = params.get(name);
            if (values == null || values.length == 0) {
                return null;
            }
            return values[0];
        }

        public String[] getParameterValues(String name) {
            return params.get(name);
        }

        private void addAllParameters(Map<String, Object> otherParams) {
            for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
                addParameter(entry.getKey(), entry.getValue());
            }
        }

        private void addParameter(String name, Object value) {//增加参数
            if (!isNull(value)) {
                if (value instanceof String[]) {
                    params.put(name, (String[]) value);
                } else {
                    params.put(name, new String[]{String.valueOf(value)});
                }

                if (value instanceof String) {
                    params.put(name, new String[]{value.toString()});
                }

            }
        }
    }
}
