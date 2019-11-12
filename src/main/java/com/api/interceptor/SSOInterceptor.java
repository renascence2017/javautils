package com.api.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Component
public class SSOInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String loginUserId = SSOUserUtil.getUserId(request);
        if (StringUtils.isBlank(loginUserId) || Objects.equals("null",loginUserId)) {
            log.error("未获取到登陆后的用户id");
            throw new BizException(ErrorEnum.NOT_LOGIN);
        } else {
            log.info("当前登录用户为：{}", loginUserId);
            SSOUserUtil.getAK(request);
            SSOUserUtil.getPermissionId(request);
            SSOUserUtil.getUserName(request);
            SSOUserUtil.getUserTag(request);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }


}