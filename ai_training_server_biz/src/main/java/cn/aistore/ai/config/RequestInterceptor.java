package cn.aistore.ai.config;

import cn.aistore.ai.service.exception.TokenException;
import cn.hutool.json.JSONUtil;
import cn.aistore.ai.common.SystemThreadLocal;
import cn.aistore.token.bean.CommonResult;
import cn.aistore.token.bean.OAuth2OpenCheckTokenRespVO;
import cn.aistore.token.bean.TokenCheckProperties;
import cn.aistore.token.service.TokenCheckService;
import com.google.common.collect.Lists;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Setter
    private TokenCheckService tokenCheckService;

    private static List<String> EXCLUDE_URLS = Lists.newArrayList("/api/aitraining/application/categories/page",
            "/api/aitraining/application/user/page", "/application/categories/page", "/application/user/page");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.info("RequestInterceptor uri: {}", uri);
        if (EXCLUDE_URLS.contains(uri)) {
            return true;
        }
        // 在请求处理之前执行的代码
        String token = request.getHeader(TokenCheckProperties.AuthHeader);
        String tenantId = request.getHeader(TokenCheckProperties.TenantHeader);
        log.info("teantId: {}, token: {}", tenantId, token);
        if (!StringUtils.hasText(token) && !StringUtils.hasText(tenantId)) {
            throw new TokenException("请先登录");
        }
        int index =  token.lastIndexOf(" ");
        if (index == -1) {
            return false;
        }
        token = token.substring(index+1);
        if (StringUtils.hasText(token) && StringUtils.hasText(tenantId)) {
//            try {
                CommonResult<OAuth2OpenCheckTokenRespVO> res = tokenCheckService.checkTokenRight(tenantId, token);
                log.info("鉴权结果：{}", JSONUtil.toJsonStr(res));
                if (res.isSuccess()) {
                    SystemThreadLocal.setTenantId(Long.parseLong(tenantId));
                    OAuth2OpenCheckTokenRespVO vo = JSONUtil.toBean(JSONUtil.toJsonStr(res.getData()), OAuth2OpenCheckTokenRespVO.class);

                    SystemThreadLocal.setUserId(vo.getUserId().intValue());
                    return true;
                }
//            }
//            catch (Exception e) {
//                log.info("鉴权异常：", e);
//                throw new TokenException("鉴权失败");
//            }
        }
        throw new TokenException("请先登录");
    }

}
