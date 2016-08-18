package com.joeyvmason.articlemanager.web.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeyvmason.articlemanager.core.application.CoreConfig;
import com.joeyvmason.articlemanager.web.application.auth.AuthenticationConfig;
import com.joeyvmason.articlemanager.web.application.auth.AuthenticationService;
import com.joeyvmason.articlemanager.web.application.auth.LoggedInAuthTokenArgumentResolver;
import com.joeyvmason.articlemanager.web.application.auth.LoggedInUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.List;

@EnableWebMvc
@Configuration
@Import({CoreConfig.class, AuthenticationConfig.class})
@PropertySource({"classpath:/conf/general.properties"})
@ComponentScan(basePackages = {"com.joeyvmason.articlemanager.web"})
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .maxAge(604800) // 1 week
                .allowedMethods("PUT", "DELETE", "POST", "GET", "PATCH")
                .allowedOrigins("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor(authenticationService));
        registry.addInterceptor(noCacheInterceptor())
                .addPathPatterns("/**");
    }

    @Override
    @DependsOn("objectMapper")
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        converters.add(new StringHttpMessageConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoggedInAuthTokenArgumentResolver(authenticationService));
        argumentResolvers.add(new LoggedInUserArgumentResolver(authenticationService));
    }

//    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver =  new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(125_829_120L);
//        return multipartResolver;
//    }

    private WebContentInterceptor noCacheInterceptor() {
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setCacheSeconds(0);
        webContentInterceptor.setUseExpiresHeader(true);
        webContentInterceptor.setUseCacheControlHeader(true);
        webContentInterceptor.setUseCacheControlNoStore(true);
        return webContentInterceptor;
    }

}