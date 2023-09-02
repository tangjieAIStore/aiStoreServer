package cn.aistore.ai.rest.controller;

import cn.aistore.ai.common.CommonRes;
import cn.hutool.json.JSONUtil;
import cn.aistore.ai.service.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        log.info("IllegalArgumentException occured: URL="+request.getRequestURL());
        return getCommonResJson(e.getLocalizedMessage());
    }

    @ExceptionHandler(value={BindException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String handleItemAlreadyExistsException(HttpServletRequest request, BindException ex) {
        log.info("ConstraintViolationException occured: URL="+request.getRequestURL());
        FieldError fieldError = ex.getFieldError();
        return getCommonResJson(fieldError.getDefaultMessage());
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public String constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return getCommonResJson(String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = TokenException.class)
    public String constraintTokenExceptionHandler(TokenException ex) {
        log.warn("[constraintTokenExceptionHandler]", ex);
        String content = ex.getMessage();
        return getCommonResJson(String.format("请求失败:%s", content));
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public String missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return getCommonResJson(String.format("请求参数缺失:%s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return getCommonResJson(String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidExceptionExceptionHandler]", ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null; // 断言，避免告警
        return getCommonResJson(String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }
    
    private String getCommonResJson(String errorMessage) {
        CommonRes<String> res = CommonRes.fail(errorMessage);
        return JSONUtil.toJsonStr(res);
    }
}
