package cn.aistore.token.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import cn.hutool.http.HttpRequest;
import cn.aistore.token.bean.CommonResult;
import cn.aistore.token.bean.OAuth2OpenCheckTokenRespVO;
import com.google.common.collect.Maps;
import cn.aistore.token.bean.TokenCheckProperties;

import java.util.Map;

//@Service
public class TokenCheckService {

//    @Autowired
    private TokenCheckProperties tokenCheckProperties;

    public TokenCheckService(TokenCheckProperties tokenCheckProperties) {
        this.tokenCheckProperties = tokenCheckProperties;
    }

    public Map<String, String> buildBasicAuthorizationHeader() {
        String authHeader = tokenCheckProperties.getClientId() + ":" + tokenCheckProperties.getClientSecret();
        Map<String, String> map = Maps.newHashMap();
        map.put(TokenCheckProperties.AuthHeader, TokenCheckProperties.AuthValPrefix + Base64.encode(authHeader));
        return map;
    }

    public CommonResult<OAuth2OpenCheckTokenRespVO> checkToken(String tenantId, String token) {

        String url = tokenCheckProperties.getUrl()+"?token="+token;
        Map<String, String> headerMap = buildBasicAuthorizationHeader();
        headerMap.put(TokenCheckProperties.TenantHeader, tenantId);
        HttpRequest httpRequest = HttpRequest.post(url).addHeaders(headerMap);
        CommonResult result = CommonResult.<OAuth2OpenCheckTokenRespVO>builder().build();
        try {
            HttpResponse response = httpRequest.execute();
            String responseS = response.body();
            result = JSONUtil.toBean(responseS, result.getClass());
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    public CommonResult<OAuth2OpenCheckTokenRespVO> checkTokenRight(String tenantId, String token) {
        CommonResult<OAuth2OpenCheckTokenRespVO> result = checkToken(tenantId, token);
        return result;
    }
}
