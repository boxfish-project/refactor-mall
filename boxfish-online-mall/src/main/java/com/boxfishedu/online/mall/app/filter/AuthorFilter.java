package com.boxfishedu.online.mall.app.filter;

import com.boxfishedu.protocal.exceptions.InvalidInputResponseBuilder;
import com.boxfishedu.protocal.premission.UserInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

@WebFilter(urlPatterns = {"/s-mall/combos"})
public class AuthorFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(AuthorFilter.class);

    @Autowired
    private UserInfoValidator userInfoValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //没有access_token的请求直接返回
        String accessToken = request.getParameter("access_token");

        if (isEmpty(accessToken) || accessToken.length() != 10) {
            InvalidInputResponseBuilder.build(response, "无效的用户标识,请勿越权使用.");
            return;
        }

        //将access_token转换为userId,如果返回null则表示用户不存在,直接返回
        Long userId = userInfoValidator.getUserId(accessToken);
        if (isNull(userId)) {
            InvalidInputResponseBuilder.build(response, "无效的用户标识,请勿越权使用.");
            return;
        }
        logger.info("请求接口 = [{}],access_token = [{}],userId = [{}]", request.getRequestURI(), accessToken, userId);
        filterChain.doFilter(request, response);
    }
}
