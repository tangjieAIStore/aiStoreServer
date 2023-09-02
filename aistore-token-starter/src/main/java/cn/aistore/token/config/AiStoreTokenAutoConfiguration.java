package cn.aistore.token.config;

import cn.aistore.token.service.TokenCheckService;
import cn.aistore.token.bean.TokenCheckProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * token校验自动配置
 *
 * @author tangking
 */
@EnableConfigurationProperties(TokenCheckProperties.class)
@Configuration
@ConditionalOnClass(TokenCheckProperties.class)
@Slf4j
public class AiStoreTokenAutoConfiguration {
    @Autowired
    private TokenCheckProperties tokenCheckProperties;

    @Bean
    @ConditionalOnMissingBean(TokenCheckService.class)
    public TokenCheckService tokenCheckService() {
        log.info("------token校验自动配置------------token---------------------------");
        return new TokenCheckService(tokenCheckProperties);
    }

}
