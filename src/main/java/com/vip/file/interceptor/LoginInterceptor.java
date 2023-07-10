package com.vip.file.interceptor;


import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.bean.Result;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.model.vo.UserLoginVO;
import com.vip.file.utils.ConstantEnum;
import com.vip.file.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zkc
 * <p>
 * 接口权限校验
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private Cache<String, Object> cache ;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (request.getHeader(ConstantEnum.ACCESS_TOKEN.getValue()) != null && !"".equals(request.getHeader(ConstantEnum.ACCESS_TOKEN.getValue()))) {
//            if (TokenUtils.verify(request.getHeader(ConstantEnum.ACCESS_TOKEN.getValue())) == null) {
//                return getResponse(response);
//            }
//            if (cache.getIfPresent(request.getHeader(ConstantEnum.ACCESS_TOKEN.getValue())) == null) {
//                return getResponse(response);
//            }
            return true;
//        }else {
//            return getResponse(response);
//        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private boolean getResponse(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(Result.failure(ResultCodeEnum.TOKEN_ERROR)));
        return false;
    }

}
