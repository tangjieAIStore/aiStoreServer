package cn.aistore.ai.config;


import cn.aistore.ai.rest.domain.BaseReq;
import cn.aistore.ai.common.IgnoreBaseReq;
import cn.aistore.ai.common.SystemThreadLocal;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 切面：统一处理BaseEntity的tenantId和userId
 */
@Component
@Aspect
public class BaseReqAspect {

    @Pointcut("execution(* cn.aistore.ai.rest.controller.*.*(..))")
    public void baseEntity() {}

    @Before("baseEntity()")
    public void around(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod ();
        Annotation[] annotations = method.getAnnotations();
        if (annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof IgnoreBaseReq && ((IgnoreBaseReq) annotation).value()) {
                    return ;
                }
            }
        }
        if (params.length > 0) {
            if (params[0] instanceof BaseReq) {
                BaseReq baseEntity = (BaseReq) params[0];
                baseEntity.setTenantId(SystemThreadLocal.getTenantId());
                baseEntity.setUserId(SystemThreadLocal.getUserId());
            }
            if (params.length > 1 && params[1] instanceof BaseReq) {
                BaseReq baseEntity = (BaseReq) params[1];
                baseEntity.setTenantId(SystemThreadLocal.getTenantId());
                baseEntity.setUserId(SystemThreadLocal.getUserId());
            }
        }
    }
}
