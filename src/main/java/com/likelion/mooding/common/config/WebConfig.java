package com.likelion.mooding.common.config;

import com.likelion.mooding.auth.presentation.argumentresolver.GuestArgumentResolver;
import com.likelion.mooding.auth.presentation.interceptor.GuestInterceptor;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String ALLOWED_ORIGIN;

    public WebConfig(@Value("${allowed-origin}") final String ALLOWED_ORIGIN) {
        this.ALLOWED_ORIGIN = ALLOWED_ORIGIN;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new GuestArgumentResolver());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new GuestInterceptor())
                .addPathPatterns("/feedback/**");
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", ALLOWED_ORIGIN)
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
