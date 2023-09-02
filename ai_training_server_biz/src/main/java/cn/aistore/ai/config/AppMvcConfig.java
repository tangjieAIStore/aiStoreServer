package cn.aistore.ai.config;

import cn.aistore.token.service.TokenCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TokenCheckService tokenCheckService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
//                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        RequestInterceptor requestInterceptor = new RequestInterceptor();
        requestInterceptor.setTokenCheckService(tokenCheckService);
        registry.addInterceptor(requestInterceptor);
    }
}
