package com.likelion.mooding.auth.presentation.interceptor;

import com.likelion.mooding.auth.exception.AuthException;
import com.likelion.mooding.auth.exception.AuthExceptionType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class GuestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        final HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            throw new AuthException(AuthExceptionType.INVALID_SESSION);
        }
        return true;
    }
}
