package com.likelion.mooding.common.config;

import com.likelion.mooding.auth.presentation.argumentresolver.GuestArgumentResolver;
import com.likelion.mooding.auth.presentation.interceptor.GuestInterceptor;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String DOMAIN_NAME;
    private final String WWW_DOMAIN_NAME;

    public WebConfig(
            @Value("${allowed-origin.domain.url}") final String DOMAIN_NAME,
            @Value("${allowed-origin.domain.www-url}") final String WWW_DOMAIN_NAME
    ) {
        this.DOMAIN_NAME = DOMAIN_NAME;
        this.WWW_DOMAIN_NAME = WWW_DOMAIN_NAME;
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
                .allowedOrigins("http://localhost:3000", DOMAIN_NAME, WWW_DOMAIN_NAME)
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.LOCATION, HttpHeaders.COOKIE, HttpHeaders.CONTENT_TYPE)
                .allowCredentials(true)
                .maxAge(3600);
    }
}
