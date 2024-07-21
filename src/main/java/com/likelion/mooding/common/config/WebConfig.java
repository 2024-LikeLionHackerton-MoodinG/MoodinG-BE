package com.likelion.mooding.common.config;

import com.likelion.mooding.auth.presentation.argumentresolver.GuestArgumentResolver;
import com.likelion.mooding.auth.presentation.interceptor.GuestInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new GuestArgumentResolver());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new GuestInterceptor())
                .addPathPatterns("/feedback/**");
    }
}
