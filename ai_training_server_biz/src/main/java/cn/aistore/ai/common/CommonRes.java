package cn.aistore.ai.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonRes<T> {
    private Integer code;
    private String msg;

    private T data;

    public static <T> CommonRes<T> success(T data) {
        return CommonRes.<T>builder().code(StatusEnum.SUCCESS.getCode()).msg(StatusEnum.SUCCESS.getMsg()).data(data).build();
    }
    public static CommonRes<String> success() {
        return CommonRes.<String>builder().code(StatusEnum.SUCCESS.getCode()).msg(StatusEnum.SUCCESS.getMsg()).data("成功").build();
    }

    public static <T> CommonRes<T> fail(T data) {
        return CommonRes.<T>builder().code(StatusEnum.FAIL.getCode()).msg(StatusEnum.FAIL.getMsg()).data(data).build();
    }

    public static CommonRes<String> fail() {
        return CommonRes.<String>builder().code(StatusEnum.FAIL.getCode()).msg(StatusEnum.FAIL.getMsg()).data("失败").build();
    }

    public static CommonRes build(Integer code, String msg) {
        return CommonRes.builder().code(code).msg(msg).build();
    }

    public static <T> CommonRes<T> build(Integer code, String msg, T obj) {
        return CommonRes.<T>builder().code(code).msg(msg).data(obj).build();
    }


    public static CommonRes build(StatusEnum statusEnum) {
        return CommonRes.builder().code(statusEnum.getCode()).msg(statusEnum.getMsg()).build();
    }
}
