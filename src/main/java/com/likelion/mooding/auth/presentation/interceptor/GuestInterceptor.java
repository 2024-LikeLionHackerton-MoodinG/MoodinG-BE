package com.likelion.mooding.auth.presentation.interceptor;

import com.likelion.mooding.auth.exception.SessionNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class GuestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        final HttpSession httpSession = request.getSession(false);
        if (httpSession == null) { // 세션 ID가 아예 없거나 잘못된 세션 ID로 요청했을 때
            throw new SessionNotFoundException();
        }
        return true;
    }
}
