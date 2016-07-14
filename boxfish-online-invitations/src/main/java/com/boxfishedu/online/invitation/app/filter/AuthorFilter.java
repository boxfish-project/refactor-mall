package com.boxfishedu.online.invitation.app.filter;

import com.boxfishedu.online.invitation.app.configure.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.fromJson;
import static com.google.common.collect.Maps.newHashMap;
import static org.springframework.util.StringUtils.isEmpty;

@WebFilter(urlPatterns = "/invitation/*")
public class AuthorFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(AuthorFilter.class);

    //  ====== > 这里的access_token是独立的,邀请码模块专用的,与其他模块的东西不一样  <=======

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getParameter("access_token");

        if (isEmpty(accessToken)) {
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        try {
            String userInfo = this.getUserInfo(accessToken);
            if (isEmpty(userInfo)) {
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            Account account = fromJson(userInfo, Account.class);

            String url = request.getRequestURI();
            if (url.contains("/invitation/create")) {
                if (!"admin".contentEquals(account.getRole())) {
                    logger.error("未经授权的用户试图创建邀请码,已被拒绝.");
                    request.getRequestDispatcher("/login").forward(request, response);
                } else {
                    Map<String, Object> params = newHashMap();
                    params.put("userId", account.getUserId());
                    AuthorRequestWrapper authorRequestWrapper = new AuthorRequestWrapper(request, params);
                    filterChain.doFilter(authorRequestWrapper, response);
                }
            } else {
                filterChain.doFilter(request, response);
            }

            logger.info("请求接口 = [{}],access_token = [{}],userId = [{}]", url, accessToken, account.getUserId());
        } catch (Exception e) {
            logger.warn("无效的用户标识!");
            e.printStackTrace();
            request.getRequestDispatcher("/login").forward(request, response);
        }
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String getUserInfo(String accessToken) throws Exception {
        return this.redisTemplate.opsForValue().get("access_token_" + accessToken);
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
            if (value != null) {
                if (value instanceof String[]) {
                    params.put(name, (String[]) value);
                } else if (value instanceof String) {
                    params.put(name, new String[]{value.toString()});
                } else {
                    params.put(name, new String[]{String.valueOf(value)});
                }
            }
        }
    }

}
