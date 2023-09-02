package cn.aistore.token.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "config.client")
public class TokenCheckProperties {
    public final static String AuthHeader = "Authorization";
    public final static String AuthValPrefix = "Basic ";
    public final static String TenantHeader = "Tenant-Id";
    private String url;
    private String clientId;
    private String clientSecret;
}
