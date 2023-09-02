package cn.aistore.ai.common;

import lombok.Getter;

public enum StatusEnum {
    SUCCESS(0, "success"),
    FAIL(500, "fail")
    ;

    @Getter
    private Integer code;

    @Getter
    private String msg;

    private StatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
