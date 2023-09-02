package cn.aistore.token.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回
 *
 * @param <T> 数据泛型
 */
@Data
@Builder
public class CommonResult<T> implements Serializable {

    /**
     * 错误码
     *
     */
    private Integer code;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 错误提示，用户可阅读
     *
     */
    private String msg;


//    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return code == 0;
    }

}
