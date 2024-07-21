package com.likelion.mooding.auth.presentation.argumentresolver;

import com.likelion.mooding.auth.annotation.Auth;
import com.likelion.mooding.auth.exception.SessionTimeoutException;
import com.likelion.mooding.auth.presentation.dto.Guest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class GuestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class) && parameter.getParameterType().equals(Guest.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory)
            throws Exception {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        final HttpSession session = request.getSession(false);

        if (session == null) {
            throw new SessionTimeoutException();
        }

        final String uuid = (String) session.getAttribute("guestId");
        return new Guest(uuid);
    }
}
