package cn.aistore.token.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
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
    //返回userid
    public static Long authenticate(String url, String bearerToken) {
        // 创建 HttpRequest 对象
        HttpRequest request = HttpRequest.get(url);

        // 添加 Authorization 头
        request.header("Authorization", "Bearer " + bearerToken);

        // 发送请求并获取 HttpResponse 对象
        HttpResponse response = request.execute();

        // 输出响应结果
        if (response.isOk()) {
            String responseBody = response.body();
            System.out.println("请求成功，响应内容：" + responseBody);

            // 解析 JSON 并获取 data 字段的值
            JSONObject jsonObject = JSONUtil.parseObj(responseBody);
            if (jsonObject.getBool("success", false)) {
                return jsonObject.getLong("data");
            } else {
                System.out.println("请求成功，但 success 字段为 false");
                return null;
            }
        } else {
            System.out.println(url+",请求失败，状态码：" + response.getStatus());
            return null;
        }
    }
    public Long checkToken(String tenantId, String token) {
        tenantId="0";
        String url = tokenCheckProperties.getUrl();
        Long userid = authenticate(url, token);
        return userid;
    }

    public Long checkTokenRight(String tenantId, String token) {
       return checkToken(tenantId, token);
    }
}
